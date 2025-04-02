package net.crystopia.packmanager

import java.nio.file.Files
import java.nio.file.Paths
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

import java.io.ByteArrayOutputStream


fun zipFolder(sourceFolderPath: String): ByteArray {
    val sourcePath = Paths.get(sourceFolderPath)
    val byteArrayOutputStream = ByteArrayOutputStream()


    ZipOutputStream(byteArrayOutputStream).use { zipOut ->
        Files.walk(sourcePath).filter { !Files.isDirectory(it) }.forEach { file ->
            val zipEntry = ZipEntry(sourcePath.relativize(file).toString())
            zipOut.putNextEntry(zipEntry)
            Files.copy(file, zipOut)
            zipOut.closeEntry()
        }
    }

    return byteArrayOutputStream.toByteArray()
}
