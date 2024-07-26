package com.example.run.presentation.tracking.service

import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.app.TaskStackBuilder
import androidx.core.net.toUri
import com.example.core.domain.dispatchers.AppDispatchers
import com.example.core.presentation.desygnsystem.R
import com.example.presentation.ui.formatted
import com.example.run.domain.RunningTracker
import com.example.run.presentation.tracking.notoification.NotificationManagerImpl
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.ext.android.inject
import org.koin.core.qualifier.named

class RunTrackingService : Service() {

    private val dispatcher by inject<CoroutineDispatcher>(named(AppDispatchers.DISPATCHER_UI))
    private var serviceScope: CoroutineScope? = null
    private val runningTracker by inject<RunningTracker>()
    private val runnersNotificationManager: com.example.run.presentation.tracking.notoification.NotificationManager by lazy {
        NotificationManagerImpl(context = applicationContext)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> {
                serviceScope = CoroutineScope(dispatcher + SupervisorJob())
                val activityClass = intent.getStringExtra(EXTRA_ACTIVITY_CLASS)
                    ?: throw IllegalArgumentException("No activity class provided")
                start(Class.forName(activityClass))
            }

            ACTION_STOP -> {
                stop()
            }
        }
        return START_STICKY
    }

    private fun start(activityClass: Class<*>) {
        if (isServiceActive.not()) {
            isServiceActive = true

            val activityIntent = Intent(applicationContext, activityClass).apply {
                data = RUNNERS_DEEP_LINK.toUri()
                addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            }
            val pendingIntent = TaskStackBuilder.create(applicationContext).run {
                addNextIntentWithParentStack(activityIntent)
                getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE)
            }

            runnersNotificationManager.showNotification(
                title = getString(com.example.run.presentation.R.string.runners),
                smallIcon = R.drawable.logo,
                pendingIntent = pendingIntent,
                startService = {
                    startForeground(1, it)
                }
            )
            updateNotification()
        }
    }

    private fun updateNotification() {
        serviceScope?.let {
            runningTracker.elapsedTime
                .onEach { elapsedTime ->
                    runnersNotificationManager.showNotification(
                        title = getString(com.example.run.presentation.R.string.runners),
                        smallIcon = R.drawable.logo,
                        description = elapsedTime.formatted()
                    )
                }
                .launchIn(it)
        }
    }

    private fun stop() {
        stopSelf()
        isServiceActive = false
        serviceScope?.cancel()
        serviceScope = null
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    companion object {
        private const val EXTRA_ACTIVITY_CLASS = "extra_activity_class"
        var isServiceActive = false
        const val RUNNERS_DEEP_LINK = "runners://run_tracking"
        const val ACTION_START = "action_start"
        const val ACTION_STOP = "action_stop"

        fun createStartIntent(context: Context, activityClass: Class<*>): Intent {
            return Intent(context, RunTrackingService::class.java).apply {
                action = ACTION_START
                putExtra(EXTRA_ACTIVITY_CLASS, activityClass.name)
            }
        }

        fun createStopIntent(context: Context): Intent {
            return Intent(context, RunTrackingService::class.java).apply {
                action = ACTION_STOP
            }
        }
    }

    enum class RunTrackingServiceState {
        START,
        STOP
    }
}