plugins {
    kotlin("jvm") version "1.6.0"
    id("com.github.johnrengelman.shadow") version "7.0.0"
    application
}

group = "me.alexirving.bot"
version = "3.0"



repositories {
    mavenCentral()
    maven("https://repo.mattmalec.com/repository/releases")
    maven("https://repo.triumphteam.dev/snapshots/")
    maven("https://jitpack.io")

}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.6.0")
    implementation("net.dv8tion:JDA:5.0.0-alpha.17")
    implementation("ch.qos.logback:logback-classic:1.2.11")
    implementation("org.litote.kmongo:kmongo:4.5.1")
    implementation("com.mattmalec:Pterodactyl4J:2.BETA_134")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.2")/*CoRoutines - Used for async*/
    implementation("com.google.code.gson:gson:2.9.0")
    implementation ("dev.triumphteam:triumph-cmd-jda-slash:2.0.0-SNAPSHOT")

    implementation("com.github.shmezi:AlexLib:1.5.1")

}
tasks {
    shadowJar {
        archiveFileName.set("PteroBot-${project.version}.jar")
        relocate("me.mattstudios.mf", "me.alexirving.pterobot.depends.mf")

    }
    application{
        mainClass.set("me.alexirving.pterobot.PteroLauncherKt")
    }
}