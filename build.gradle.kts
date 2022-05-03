plugins {
    kotlin("jvm") version "1.6.0"
    id("com.github.johnrengelman.shadow") version  "5.2.0"

}

group = "me.alexirving.bot"
version = "3.0"


repositories {
    mavenCentral()
    maven("https://repo.mattmalec.com/repository/releases")
    maven("https://repo.triumphteam.dev/snapshots/")
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.6.0")
    implementation("net.dv8tion:JDA:5.0.0-alpha.2")
    implementation("ch.qos.logback:logback-classic:1.2.7")
    implementation("org.yaml:snakeyaml:1.29")
    implementation("org.litote.kmongo:kmongo-coroutine:4.5.1")
    implementation("com.zaxxer:HikariCP:5.0.0")
    implementation("com.mattmalec:Pterodactyl4J:2.BETA_120")
    implementation("mysql:mysql-connector-java:8.0.27")
    implementation("mysql:mysql-connector-java:8.0.27")
    implementation("org.xerial:sqlite-jdbc:3.36.0.3")
    implementation("dev.triumphteam:triumph-cmd-jda:2.0.0-SNAPSHOT")


}
tasks {
    shadowJar {
        archiveFileName.set("PteroBot-${project.version}.jar")
        relocate("me.mattstudios.mf", "me.alexirving.pterobot.depends.mf")

    }
    jar {
        manifest {
            attributes("Main-Class" to "me.alexirving.pterobot.PteroLauncher")
        }
    }
}