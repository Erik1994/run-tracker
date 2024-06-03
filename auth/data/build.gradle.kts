plugins {
    alias(libs.plugins.run.tracker.android.library)
    alias(libs.plugins.run.tracker.jvm.ktor)
}

android {
    namespace = "com.example.auth.data"
}

dependencies {
    implementation(projects.auth.domain)
    implementation(projects.core.domain)
    implementation(projects.core.data)
}