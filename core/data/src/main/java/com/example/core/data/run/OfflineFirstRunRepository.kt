package com.example.core.data.run

import com.example.core.data.network.Endpoint
import com.example.core.data.network.get
import com.example.core.database.dao.RunPendingSyncDao
import com.example.core.database.mapper.toRun
import com.example.core.domain.auth.SessionStorage
import com.example.core.domain.dispatchers.AppDispatchers
import com.example.core.domain.run.LocalRunDataSource
import com.example.core.domain.run.RemoteRunDataSource
import com.example.core.domain.run.Run
import com.example.core.domain.run.RunId
import com.example.core.domain.run.RunRepository
import com.example.core.domain.run.SyncRunScheduler
import com.example.core.domain.util.DataError
import com.example.core.domain.util.EmptyResult
import com.example.core.domain.util.Result
import com.example.core.domain.util.asEmptyDataResult
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerAuthProvider
import io.ktor.client.plugins.plugin
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.bson.types.ObjectId

class OfflineFirstRunRepository(
    private val localRunDataSource: LocalRunDataSource,
    private val remoteRunDataSource: RemoteRunDataSource,
    private val applicationScope: CoroutineScope,
    private val runPendingSyncDao: RunPendingSyncDao,
    private val sessionStorage: SessionStorage,
    private val appDispatchers: AppDispatchers,
    private val syncRunScheduler: SyncRunScheduler,
    private val client: HttpClient
) : RunRepository {
    override fun getRuns(): Flow<List<Run>> {
        return localRunDataSource.getRuns()
    }

    override suspend fun fetchRuns(): EmptyResult<DataError> {
        return when (val result = remoteRunDataSource.getRuns()) {
            is Result.Error -> result.asEmptyDataResult()
            is Result.Success -> applicationScope.async {
                localRunDataSource.upsertRuns(result.data).asEmptyDataResult()
            }.await()
        }
    }

    override suspend fun upsertRun(run: Run, mapPicture: ByteArray): EmptyResult<DataError> {
//        val localResult = localRunDataSource.upsertRun(run)
//        if (localResult !is Result.Success) {
//            return localResult.asEmptyDataResult()
//        }

        val runWithId = run.copy(id = ObjectId().toHexString())
        val remoteResult = remoteRunDataSource.postRun(
            run = runWithId,
            mapPicture = mapPicture
        )

        return when (remoteResult) {
            is Result.Error -> {
                applicationScope.launch {
                    syncRunScheduler.scheduleSync(
                        type = SyncRunScheduler.SyncType.CreateRun(
                            run = runWithId,
                            mapPictureBytes = mapPicture
                        )
                    )
                }.join()
                Result.Success(Unit)
            }

            is Result.Success -> applicationScope.async {
                localRunDataSource.upsertRun(remoteResult.data).asEmptyDataResult()
            }.await()
        }
    }

    override suspend fun deleteRun(id: RunId) {
        localRunDataSource.deleteRun(id)

        // Handle the edge case where the run is created
        // and deleted in offline-mode,
        // In that case, we don't need to sync anything
        val isPendingSync = runPendingSyncDao.getRunPendingSyncEntity(id) != null
        if (isPendingSync) {
            runPendingSyncDao.deleteRunPendingSynEntity(id)
            return
        }

        val remoteResult = applicationScope.async {
            remoteRunDataSource.deleteRun(id)
        }.await()

        if (remoteResult is Result.Error) {
            applicationScope.launch {
                syncRunScheduler.scheduleSync(
                    type = SyncRunScheduler.SyncType.DeleteRun(id)
                )
            }.join()
        }
    }

    override suspend fun syncPendingRuns() {
        withContext(appDispatchers.ioDispatcher) {
            val userId = sessionStorage.get()?.userId ?: return@withContext
            val createdRuns = async {
                runPendingSyncDao.getAllRunPendingSyncEntity(userId)
            }
            val deletedRuns = async {
                runPendingSyncDao.getAllDeletedRunSyncEntities(userId)
            }

            val createJobs = createdRuns
                .await()
                .map { pendingRun ->
                    launch {
                        val run = pendingRun.run.toRun()
                        when (remoteRunDataSource.postRun(run, pendingRun.mapPictureBytes)) {
                            is Result.Error -> Unit
                            is Result.Success -> applicationScope.launch {
                                runPendingSyncDao.deleteRunPendingSynEntity(pendingRun.runId)
                            }.join()
                        }
                    }
                }

            val deleteJobs = deletedRuns
                .await()
                .map { deletedRun ->
                    launch {
                        when (remoteRunDataSource.deleteRun(deletedRun.runId)) {
                            is Result.Error -> Unit
                            is Result.Success -> applicationScope.launch {
                                runPendingSyncDao.deleteDeletedRunSyncEntity(deletedRun.runId)
                            }.join()
                        }
                    }
                }

            createJobs.joinAll()
            deleteJobs.joinAll()
        }
    }

    override suspend fun logOut(): EmptyResult<DataError.Network> {
        val result = client.get<Unit>(
            endpoint = Endpoint.LogOut
        ).asEmptyDataResult()

        client.plugin(Auth).providers.filterIsInstance<BearerAuthProvider>()
            .firstOrNull()
            ?.clearToken()

        return result
    }

    override suspend fun deleteAllRuns() {
        localRunDataSource.deleteAllRuns()
    }
}