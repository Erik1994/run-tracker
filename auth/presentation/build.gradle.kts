plugins {
    alias(libs.plugins.run.tracker.android.feature.ui)
}

android {
    namespace = "com.example.auth.presentation"
}

dependencies {
    implementation(projects.auth.domain)
    implementation(projects.core.domain)
}