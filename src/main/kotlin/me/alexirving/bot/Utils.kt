package me.alexirving.bot

import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption

/**
 * Copies a file from resources with the given name.
 * @param name Name of file to be copied
 */
fun copyIfNotExist(name: String) {
    if (!File(name).exists())
        Files.copy(
            Thread.currentThread().contextClassLoader.getResourceAsStream(name),
            Path.of(name),
            StandardCopyOption.REPLACE_EXISTING
        )
}