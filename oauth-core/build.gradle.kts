@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    with(catalogPlugins.plugins) {
        plugin(android.library)
        plugin(kotlin.multiplatform)
        plugin(moko.multiplatform)
        plugin(kotlin.serialization)
        id(mersey.kotlin.convention.id())
        id(mersey.android.convention.id())
    }
    `maven-publish-config`
}

android {
    namespace = "com.merseyside.merseyLib.oauth.core"
    compileSdk = Application.compileSdk

    defaultConfig {
        minSdk = Application.minSdk
        targetSdk = Application.targetSdk
    }

    buildFeatures {
        dataBinding = true
    }
}

kotlin {
    android {
        publishLibraryVariants("release", "debug")
        publishLibraryVariantsGroupedByFlavor = true
    }

    ios()
    iosSimulatorArm64()

    sourceSets {
        val iosMain by getting
        val iosSimulatorArm64Main by getting
        iosSimulatorArm64Main.dependsOn(iosMain)
    }
}


kotlinConvention {
    debug = true
    setCompilerArgs(
        "-Xinline-classes",
        "-opt-in=kotlin.RequiresOptIn",
        "-Xskip-prerelease-check"
    )
}

dependencies {
    commonMainImplementation(common.serialization)

    if (isLocalKotlinExtLibrary()) {
        commonMainApi(project(Modules.MultiPlatform.MerseyLibs.kotlinExt))
    } else {
        commonMainApi(common.mersey.kotlin.ext)
    }
}