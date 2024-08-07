plugins {
    alias(libs.plugins.run.tracker.android.library)
    alias(libs.plugins.run.tracker.jvm.ktor)
}

android {
    namespace = "com.example.core.data"
}

dependencies {
    implementation(libs.timber)
    implementation(libs.org.mongodb.bson)

    implementation(projects.core.domain)
    implementation(projects.core.database)
}