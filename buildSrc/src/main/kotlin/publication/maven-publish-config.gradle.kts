import java.util.Base64

plugins {
    id("javadoc-stub-convention")
    `maven-publish`
    signing
}

publishing {
    publications.withType<MavenPublication>().all {
        pom {
            name.set("Mersey OAuth kmm library")
            description.set("OAuth library for mobile multiplatform")
            url.set("https://github.com/Merseyside/mersey-kmp-oauth")

            licenses {
                license {
                    name.set("MIT")
                    url.set("https://opensource.org/licenses/MIT")
                }
            }
            developers {
                developer {
                    id.set("Merseyside")
                    name.set("Ivan Sablin")
                    email.set("ivanklessablin@gmail.com")
                }
            }
            scm {
                url.set("https://github.com/Merseyside/mersey-kmp-oauth")
            }
        }
    }
}

signing {
    val signingKeyId: String? = System.getenv("SIGNING_KEY_ID")
    val signingPassword: String? = System.getenv("SIGNING_PASSWORD")
    val signingKey: String? = System.getenv("SIGNING_KEY")?.let { base64Key ->
        val _base = base64Key.replace("\n", "")
        String(Base64.getDecoder().decode(_base))
    }
    if (signingKeyId != null) {
        useInMemoryPgpKeys(signingKeyId, signingKey, signingPassword)
        sign(publishing.publications)
    }
}