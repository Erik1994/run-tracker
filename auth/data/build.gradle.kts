plugins {
    alias(libs.plugins.run.tracker.android.library)
    alias(libs.plugins.run.tracker.jvm.ktor)
}

android {
    namespace = "com.example.auth.data"
}

dependencies {
    implementation(platform(projects.auth.domain))
    implementation(platform(projects.core.domain))
    implementation(platform(projects.core.data))
}