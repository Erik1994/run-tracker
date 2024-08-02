plugins {
    alias(libs.plugins.run.tracker.android.library)
    alias(libs.plugins.run.tracker.android.room)
}

android {
    namespace = "com.example.analytics.data"
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(projects.core.database)
    implementation(projects.core.domain)
    implementation(projects.analytics.domain)
}