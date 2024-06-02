plugins {
    alias(libs.plugins.run.tracker.android.library)
    alias(libs.plugins.run.tracker.android.room)
}

android {
    namespace = "com.example.core.database"
}

dependencies {
    implementation(libs.org.mongodb.bson)

    implementation(projects.core.domain)
}