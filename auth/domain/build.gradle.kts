plugins {
    alias(libs.plugins.run.tracker.jvm.library)
}

dependencies {
    implementation(projects.core.domain)
}