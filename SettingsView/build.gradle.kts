plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-android")
    id("kotlin-parcelize")
    id("maven-publish")
}

android {
    namespace = "io.github.kaztakgh.settingsview"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
        version = "0.2.1.1"
        multiDexEnabled = true

        // aarファイル名の設定
        setProperty("archivesBaseName", "settingsview-${version}")
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    // dataBindingはテストで使用できるようになってから検討
//    buildFeatures {
//        dataBinding = true
//    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    packaging {
        jniLibs {
            excludes.add("assets/*")
            excludes.add("META-INF/*")
        }
    }
    testOptions {
        // for Robolectric
        // resourceを利用可能にする
        unitTests.isIncludeAndroidResources = true
        // for Mockito
        unitTests.isReturnDefaultValues = true
    }
    // viewBindingはテストで使用できるようになってから検討
//    viewBinding {
//        enable = true
//    }
}

dependencies {

    // UI関連
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
//    implementation("androidx.databinding:databinding-runtime:8.2.0")
//    implementation("androidx.databinding:databinding-ktx:8.2.0")

    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.21")
    implementation("com.android.support:multidex:1.0.3")

    // テスト関連
    testImplementation("junit:junit:4.13.2")
    testImplementation("androidx.test.ext:junit:1.1.5")
    testImplementation("androidx.test.ext:junit-ktx:1.1.5")
    androidTestImplementation("androidx.test:core:1.5.0")
    androidTestImplementation("androidx.test:core-ktx:1.5.0")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.ext:junit-ktx:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.test:rules:1.5.0")
    androidTestImplementation("androidx.test:runner:1.5.2")
    androidTestImplementation("androidx.test.uiautomator:uiautomator:2.3.0")

    // Robolectric
    testImplementation("org.robolectric:robolectric:4.11.1")

    // Mockito
    testImplementation("org.mockito:mockito-core:5.8.0")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.2.1")
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components.findByName("java"))
            pom {
                groupId = "io.github.kaztakgh"
                artifactId = "settings-view"
                version = "0.2.1.1"
                artifact("$buildDir/outputs/aar/settingsview-$version-release.aar")
            }
        }
    }
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/kaztakgh/SettingsView")
            credentials {
                username = findProperty("gpr.user") as String? ?: System.getenv("USERNAME")
                password = findProperty("gpr.key") as String? ?: System.getenv("TOKEN")
            }
        }
    }
}

tasks.register("publishToGitHubPackages") {
    dependsOn(":SettingsView:publishMavenJavaPublicationToGitHubPackagesRepository")
}
