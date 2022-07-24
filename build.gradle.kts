plugins {
    alias(libs.plugins.indra)
    alias(libs.plugins.indra.checkstyle)
    alias(libs.plugins.indra.publishing)
    alias(libs.plugins.indra.publishing.sonatype)
}

indra {
    javaVersions {
        minimumToolchain(17)
        target(17)
        testWith(17)
    }

    checkstyle(libs.versions.checkstyle.get())

    github("TozyMC", "plugin-loader") {
        ci(true)
    }
    mitLicense()

    configurePublications {
        pom {
            developers {
                developer {
                    id.set("TozyMC")
                    name.set("Nguyễn Thanh Tân")
                    email.set("tozymc@gmail.com")
                }
            }
        }
    }
}

dependencies {
    compileOnlyApi(libs.jetbrains.annotations)

    api(libs.slf4j)

    implementation(libs.gson)
}
