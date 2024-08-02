plugins {
    alias(libs.plugins.run.tracker.android.dynamic.feature)
}
android {
    namespace = "com.example.analytics.analytics_feature"
}

dependencies {
    implementation(project(":app"))
    api(projects.analytics.presentation)
    implementation(projects.analytics.domain)
    implementation(projects.analytics.data)
    implementation(projects.core.database)
}