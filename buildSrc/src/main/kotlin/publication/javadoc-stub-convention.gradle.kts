plugins {
    id("maven-publish-config")
}

val javadocJar by tasks.registering(Jar::class) {
    archiveClassifier.set("javadoc")
}

publishing.publications.withType<MavenPublication> {
    // Stub javadoc.jar artifact
    artifact(javadocJar.get())
}