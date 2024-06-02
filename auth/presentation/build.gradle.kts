plugins {
    alias(libs.plugins.run.tracker.android.feature.ui)
}

android {
    namespace = "com.example.auth.presentation"
}

dependencies {
    implementation(platform(projects.auth.domain))
    implementation(platform(projects.core.domain))
}