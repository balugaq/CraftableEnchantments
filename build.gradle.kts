plugins {
    java
    alias(libs.plugins.shadow)
    alias(libs.plugins.run.paper)
}

group = "io.github.schntgaispock.craftableenchantments"
version = "1.0.0"

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://jitpack.io")
}

dependencies {
    compileOnly(libs.spigot.api)
    compileOnly(libs.slimefun4)
    implementation(libs.infinity.lib)
    implementation(libs.bstats)
    compileOnly(libs.javax.annotation)
    compileOnly(libs.jsr305)
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)

    testImplementation(platform("org.junit:junit-bom:5.10.2"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<JavaExec>().configureEach {
    systemProperty("file.encoding", "UTF-8")
    systemProperty("sun.stdout.encoding", "UTF-8")
    systemProperty("sun.stderr.encoding", "UTF-8")
}

tasks {
    compileJava {
        options.compilerArgs.add("-Xlint:-removal")
        options.release = 21
    }

    val pluginVersion = project.version.toString()
    processResources {
        filesMatching("plugin.yml") {
            expand("version" to pluginVersion)
        }
    }

    shadowJar {
        archiveBaseName.set("CraftableEnchantments")
        archiveVersion.set(project.version.toString())
        archiveClassifier.set("")

        minimize()

        // Relocations（沿袭原 pom.xml）
        relocate("io.github.mooy1.infinitylib", "io.github.schntgaispock.infinitylib")
        relocate("org.bstats", "io.github.schntgaispock.craftableenchantments.bstats")

        exclude("META-INF/*")

        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        mergeServiceFiles()
    }

    build {
        dependsOn(shadowJar)
    }

    runServer {
        dependsOn(shadowJar)
        val run = file(providers.gradleProperty("server.run.dir").orElse("run"))
        runDirectory.set(run)

        // configuration cache 禁止执行期访问 Project，路径在配置期取好
        val projDir = projectDir
        val jarName = "${name}-${version}.jar"
        doFirst {
            run.resolve("eula.txt").writeText("eula=true")

            val pl = run.resolve("plugins")
            pl.mkdirs()
            copy {
                from(projDir.resolve("build/libs")) {
                    include(jarName)
                }
                into(pl)
            }
        }

        jvmArgs(
            "-Dfile.encoding=UTF-8",
            "-Dsun.jnu.encoding=UTF-8"
        )
        maxHeapSize = "2G"
        minecraftVersion("1.20.1")
    }
}

defaultTasks("clean", "build")
