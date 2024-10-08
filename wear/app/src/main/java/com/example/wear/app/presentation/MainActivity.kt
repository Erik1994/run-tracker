package com.example.wear.app.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.core.notification.service.RunTrackingService
import com.example.core.presentation.designsystem_wear.RunnersTheme
import com.example.wear.run.presentation.TrackerScreenRoot

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)

        setTheme(android.R.style.Theme_DeviceDefault)

        setContent {
            RunnersTheme {
                TrackerScreenRoot(onServiceToggle = { runTrackingServiceState ->
                    when (runTrackingServiceState) {
                        RunTrackingService.RunTrackingServiceState.START -> startService(
                            RunTrackingService.createStartIntent(
                                applicationContext,
                                this::class.java
                            )
                        )

                        RunTrackingService.RunTrackingServiceState.STOP -> startService(
                            RunTrackingService.createStopIntent(applicationContext)
                        )
                    }
                })
            }
        }
    }
}