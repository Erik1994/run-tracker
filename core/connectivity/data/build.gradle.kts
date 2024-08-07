plugins {
    alias(libs.plugins.run.tracker.android.library)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.example.core.connectivity.data"
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.play.services)
    implementation(libs.play.services.wearable)
    implementation(libs.kotlinx.serialization.json)

    implementation(projects.core.domain)
    implementation(projects.core.connectivity.domain)
}