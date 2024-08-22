package com.example.core.notification.service

import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.app.TaskStackBuilder
import androidx.core.net.toUri
import com.example.core.domain.dispatchers.AppDispatchers
import com.example.core.notification.notoification.NotificationManager
import com.example.core.notification.notoification.NotificationManagerImpl
import com.example.core.presentation.desygnsystem.R
import com.example.presentation.ui.formatted
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.ext.android.inject
import org.koin.core.qualifier.named
import kotlin.time.Duration

class RunTrackingService : Service() {

    private val dispatcher by inject<CoroutineDispatcher>(named(AppDispatchers.DISPATCHER_UI))
    private var serviceScope: CoroutineScope? = null
    private val elapsedTime by inject<StateFlow<Duration>>()
    private val runnersNotificationManager: NotificationManager by lazy {
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
        if (isServiceActive.value.not()) {
            _isServiceActive.value = true

            val activityIntent = Intent(applicationContext, activityClass).apply {
                data = RUNNERS_DEEP_LINK.toUri()
                addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            }
            val pendingIntent = TaskStackBuilder.create(applicationContext).run {
                addNextIntentWithParentStack(activityIntent)
                getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE)
            }

            runnersNotificationManager.showNotification(
                title = getString(R.string.runners),
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
            elapsedTime
                .onEach { elapsedTime ->
                    runnersNotificationManager.showNotification(
                        title = getString(R.string.runners),
                        smallIcon = R.drawable.logo,
                        description = elapsedTime.formatted()
                    )
                }
                .launchIn(it)
        }
    }

    private fun stop() {
        stopSelf()
        _isServiceActive.value = false
        serviceScope?.cancel()
        serviceScope = null
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    companion object {
        private const val EXTRA_ACTIVITY_CLASS = "extra_activity_class"
        private val _isServiceActive = MutableStateFlow(false)
        val isServiceActive = _isServiceActive.asStateFlow()
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