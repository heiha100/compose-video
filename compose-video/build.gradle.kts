import org.jetbrains.dokka.gradle.DokkaMultiModuleTask

apply(from = "${rootDir}/publish.gradle")
//apply(from = "${rootDir}/scripts/publish-root.gradle")
//apply(from = "${rootDir}/scripts/publish-module.gradle")

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("me.tylerbwong.gradle.metalava") version "0.3.2"
    id("org.jetbrains.dokka")
    id("maven-publish")
}

metalava {
    filename.set("api/current.api")
    reportLintsAsErrors.set(true)
}

android {
    namespace = "io.sanghun.compose.video"
    compileSdk = 33

    defaultConfig {
        minSdk = 23
        targetSdk = 32

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildFeatures {
        compose = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
}

afterEvaluate {
    tasks.named("dokkaHtmlPartial") {

    }
}

dependencies {
    implementation(libs.androidx.core)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.media)
    implementation(libs.bundles.compose)
    implementation(libs.bundles.media3)
    implementation(libs.material2)

    debugImplementation(libs.bundles.compose.debugOnly)

    testImplementation(libs.junit)
    androidTestImplementation(libs.bundles.androidTest)
}


publishing {
    publications {
        create<MavenPublication>("release") {
            groupId = "ai.looktech.glass"  // 替换为你的 Group ID
            artifactId = "video-compose"           // 替换为你的 Artifact ID
            version = "1.0.1"                  // 替换为你的版本号

            // 指定发布的 AAR 文件
            artifact("$buildDir/outputs/aar/${project.name}-release.aar")

            // 配置 POM 信息
            pom {
                name.set("VideoCompose")
                description.set("A description of the library.")
                url.set("https://github.com/heiha100/compose-video")

                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0")
                    }
                }
                developers {
                    developer {
                        id.set("heiha")
                        name.set("moven")
                        email.set("01sr@outlook.com")
                    }
                }
                scm {
                    connection.set("scm:git:git@github.com:heiha100/compose-video.git")
//                    developerConnection.set("scm:git:ssh://github.com/example/mylibrary.git")
                    url.set("https://github.com/heiha100/compose-video")
                }
            }
        }
    }

    // 发布到本地 Maven 仓库
    repositories {
        maven {
            url = uri("file://${buildDir}/../repo") // 指定本地发布路径
        }
    }
}

