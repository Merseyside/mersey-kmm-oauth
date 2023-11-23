allprojects {
    plugins.withId("org.gradle.maven-publish") {
        group = "io.github.merseyside"
        version = multiplatformLibs.versions.mersey.oauth.get()
    }
}

tasks.register("clean", Delete::class).configure {
    group = "build"
    delete(rootProject.layout.buildDirectory)
}

val javadocDisabledModules = listOf(
    "oauth-core",
    "oauth-android"
)

subprojects {
    gradle.taskGraph.whenReady {
        if (javadocDisabledModules.contains(this@subprojects.name)) {
            tasks.matching { it.name == "javaDocReleaseGeneration" }.configureEach {
                // See: https://youtrack.jetbrains.com/issue/KTIJ-19005/JDK-17-PermittedSubclasses-requires-ASM9-exception-multiple-times-per-second-during-analysis
                enabled = false
            }
        }
    }
}