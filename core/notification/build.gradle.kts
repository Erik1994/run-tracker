plugins {
    alias(libs.plugins.run.tracker.android.library)
}

android {
    namespace = "com.example.core.notification"
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(projects.core.domain)
    implementation(projects.core.presentation.ui)
    implementation(projects.core.presentation.desygnsystem)
}