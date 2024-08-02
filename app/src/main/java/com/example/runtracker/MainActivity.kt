package com.example.runtracker

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.example.core.presentation.desygnsystem.RunnersTheme
import com.example.core.presentation.desygnsystem.dimentions.LocalDimensions
import com.example.runtracker.navigation.NavigationRoot
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.google.android.play.core.splitinstall.SplitInstallStateUpdatedListener
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val ANALYTICS_MODULE_NAME = "analytics_feature"
private const val ANALYTICS_ACTIVITY_CLASS_NAME =
    "com.example.analytics.analytics_feature.AnalyticsActivity"

class MainActivity : ComponentActivity() {

    private val viewModel by viewModel<MainViewModel>()
    private var splitInstallManager: SplitInstallManager? = null
    private val splitInstallListener = SplitInstallStateUpdatedListener { state ->
        when (state.status()) {
            SplitInstallSessionStatus.INSTALLED -> {
                viewModel.setAnalyticsDialogVisibility(false)
                Toast.makeText(
                    applicationContext,
                    R.string.analytics_installed,
                    Toast.LENGTH_LONG
                ).show()
            }

            SplitInstallSessionStatus.INSTALLING -> {
                viewModel.setAnalyticsDialogVisibility(true)
                viewModel.updateAnalyticsFeatureModuleState(getString(R.string.installing))
            }

            SplitInstallSessionStatus.DOWNLOADING -> {
                viewModel.setAnalyticsDialogVisibility(true)
                viewModel.updateAnalyticsFeatureModuleState(getString(R.string.downloading))
            }

            SplitInstallSessionStatus.REQUIRES_USER_CONFIRMATION -> {
                splitInstallManager?.startConfirmationDialogForResult(state, this, 0)
            }

            SplitInstallSessionStatus.FAILED -> {
                viewModel.setAnalyticsDialogVisibility(false)
                Toast.makeText(
                    applicationContext,
                    R.string.error_installation_failed,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.state.isCheckingAuth
            }
        }
        splitInstallManager = SplitInstallManagerFactory.create(applicationContext)
        enableEdgeToEdge()
        setContent {
            RunnersTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (viewModel.state.isCheckingAuth.not()) {
                        val navController = rememberNavController()
                        NavigationRoot(
                            navController = navController,
                            isLoggedIn = viewModel.state.isLoggedIn,
                            onAnalyticsClick = {
                                installOrStartAnalyticsFeature()
                            }
                        )
                        val dimensions = LocalDimensions.current
                        if (viewModel.state.showAnalyticsInstallDialog) {
                            Dialog(onDismissRequest = {}) {
                                Column(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(15.dp))
                                        .background(
                                            color = MaterialTheme.colorScheme.surface,
                                            shape = RoundedCornerShape(15.dp)
                                        )
                                        .padding(dimensions.dimenMedium),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                                    Spacer(modifier = Modifier.height(dimensions.dimenSmall))
                                    Text(
                                        text = viewModel.state.analyticsFeatureModuleState,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        splitInstallManager?.registerListener(splitInstallListener)
    }

    override fun onPause() {
        super.onPause()
        splitInstallManager?.unregisterListener(splitInstallListener)
    }

    private fun installOrStartAnalyticsFeature() {
        splitInstallManager?.let { manager ->
            if (manager.installedModules.contains(ANALYTICS_MODULE_NAME)) {
                Intent()
                    .setClassName(packageName, ANALYTICS_ACTIVITY_CLASS_NAME)
                    .also(::startActivity)
                return
            }

            val request = SplitInstallRequest.newBuilder()
                .addModule(ANALYTICS_MODULE_NAME)
                .build()
            manager.startInstall(request)
                .addOnFailureListener {
                    it.printStackTrace()
                    Toast.makeText(
                        applicationContext,
                        R.string.error_couldnt_load_module,
                        Toast.LENGTH_LONG
                    ).show()
                }
        }
    }
}