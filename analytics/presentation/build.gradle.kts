plugins {
    alias(libs.plugins.run.tracker.android.feature.ui)
}

android {
    namespace = "com.example.analytics.presentation"
}

dependencies {
    implementation(projects.analytics.domain)
}