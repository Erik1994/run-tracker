plugins {
    alias(libs.plugins.run.tracker.jvm.library)
    alias(libs.plugins.jetbrains.kotlin.jvm)
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
}