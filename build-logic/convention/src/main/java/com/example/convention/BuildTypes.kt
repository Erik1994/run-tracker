package com.example.convention

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.BuildType
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

internal fun Project.configureBuildTypes(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
    extensionType: ExtensionType
) {
    commonExtension.run {
        buildFeatures {
            buildConfig = true
        }

        val apiKey = gradleLocalProperties(
            projectRootDir = rootDir,
            providers = providers
        ).getProperty(API_KEY)

        when (extensionType) {
            ExtensionType.APPLICATION -> {
                extensions.configure<ApplicationExtension> {
                    buildTypes {
                        debug {
                            configureDebugBuildType(apiKey = apiKey)
                        }
                        release {
                            configureReleaseBuildType(
                                apiKey = apiKey,
                                commonExtension = this@run
                            )
                        }
                    }
                }
            }

            ExtensionType.LIBRARY -> {
                extensions.configure<LibraryExtension> {
                    buildTypes {
                        debug {
                            configureDebugBuildType(apiKey = apiKey)
                        }
                        release {
                            configureReleaseBuildType(
                                apiKey = apiKey,
                                commonExtension = this@run
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun BuildType.configureDebugBuildType(apiKey: String) {
    buildConfigField(
        type = "String",
        name = API_KEY,
        value = "\"$apiKey\""
    )
    buildConfigField(
        type = "String",
        name = "BASE_URL",
        value = "\"https://runique.pl-coding.com:8080\""
    )
}

private fun BuildType.configureReleaseBuildType(
    apiKey: String,
    commonExtension: CommonExtension<*, *, *, *, *, *>
) {
    buildConfigField(
        type = "String",
        name = API_KEY,
        value = "\"$apiKey\""
    )
    buildConfigField(
        type = "String",
        name = "BASE_URL",
        value = "\"https://runique.pl-coding.com:8080\""
    )

    isMinifyEnabled = true
    proguardFiles(
        commonExtension.getDefaultProguardFile("proguard-android-optimize.txt"),
        "proguard-rules.pro"
    )
}

private const val API_KEY = "API_KEY"