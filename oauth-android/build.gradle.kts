@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    with(catalogPlugins.plugins) {
        plugin(android.library)
        plugin(kotlin.android)
        id(mersey.android.convention.id())
        id(mersey.kotlin.convention.id())
    }
    `android-maven-publish-config`
}

android {
    namespace = "com.merseyside.merseyLib.oauth.android"
    compileSdk = Application.compileSdk

    defaultConfig {
        minSdk = Application.minSdk
        targetSdk = Application.targetSdk
    }
    
    buildFeatures {
        dataBinding = true
    }

    packagingOptions {
        resources.excludes.addAll(
            listOf(
                "META-INF/*.kotlin_module",
                "META-INF/DEPENDENCIES",
                "META-INF/NOTICE",
                "META-INF/LICENSE",
                "META-INF/LICENSE.txt",
                "META-INF/NOTICE.txt"
            )
        )
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

val androidLibraries = listOf(
    common.serialization,
    androidLibs.appCompat
)

val merseyModules = listOf(
    Modules.Android.MerseyLibs.archy,
    Modules.Android.MerseyLibs.utils
)

val merseyLibs = listOf(
    androidLibs.mersey.archy,
    androidLibs.mersey.utils
)

dependencies {
    api(project(":oauth-core"))

    if (isLocalAndroidDependencies()) {
        merseyModules.forEach { module -> implementation(project(module)) }
    } else {
        merseyLibs.forEach { lib -> implementation(lib) }
    }

    androidLibraries.forEach { lib -> implementation(lib) }
}