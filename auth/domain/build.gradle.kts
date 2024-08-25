plugins {
    alias(libs.plugins.run.tracker.jvm.library)
    alias(libs.plugins.run.tracker.jvm.junit5)
}

dependencies {
    implementation(projects.core.domain)
}