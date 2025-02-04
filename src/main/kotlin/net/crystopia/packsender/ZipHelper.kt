package net.crystopia.packsender

import java.nio.file.Files
import java.nio.file.Paths
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

fun zipFolder(sourceFolderPath: String, zipPath: String) {
    val sourcePath = Paths.get(sourceFolderPath)
    ZipOutputStream(Files.newOutputStream(Paths.get(zipPath))).use { zipOut ->
        Files.walk(sourcePath).filter { !Files.isDirectory(it) }.forEach { file ->
            val zipEntry = ZipEntry(sourcePath.relativize(file).toString())
            zipOut.putNextEntry(zipEntry)
            Files.copy(file, zipOut)
            zipOut.closeEntry()
        }
    }
}