plugins {
    `kotlin-dsl`
}

dependencies {
    with(catalogGradle) {
        implementation(mersey.gradlePlugins)
        implementation(android.gradle.stable)
        implementation(kotlin.gradle)
        implementation(kotlin.serialization)
        implementation(maven.publish.plugin)
    }
}
