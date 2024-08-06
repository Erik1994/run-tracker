plugins {
    alias(libs.plugins.run.tracker.android.library.compose)
}

android {
    namespace = "com.example.core.presentation.designsystem_wear"

    defaultConfig {
        minSdk = 30
    }
}

dependencies {
    api(projects.core.presentation.desygnsystem)
    implementation(libs.androidx.wear.compose.material)
}