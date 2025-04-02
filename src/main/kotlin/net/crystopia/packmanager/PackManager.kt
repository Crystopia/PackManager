package net.crystopia.packmanager

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import net.crystopia.packmanager.config.ConfigManager
import net.crystopia.packmanager.events.NexoGeneratePack
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.io.IOException

class PackManager : JavaPlugin() {

    companion object {
        lateinit var instance: PackManager
    }

    init {
        instance = this
    }

    override fun onEnable() {
        logger.info("Enabling PackSender")
        server.pluginManager.registerEvents(PlayerJoinEvent(), this)
        server.messenger.registerOutgoingPluginChannel(this, "BungeeCord")
        server.pluginManager.registerEvents(NexoGeneratePack, this)

        // Start Web API
        embeddedServer(Netty, port = ConfigManager.settings.APIPort ?: 8080) {
            routing {
                get("/") {
                    val zipFilePath = ConfigManager.settings.RPzipFilePath
                    if (zipFilePath.isNullOrBlank()) {
                        call.respond(HttpStatusCode.InternalServerError, "Invalid zip file path in configuration.")
                        return@get
                    }

                    val file = File(zipFilePath)
                    if (!file.exists() || !file.isFile) {
                        call.respond(HttpStatusCode.NotFound, "404 - No file found!")
                        return@get
                    }

                    try {
                        call.response.header(HttpHeaders.ContentDisposition, "attachment; filename=\"pack.zip\"")
                        call.response.header(HttpHeaders.ContentType, ContentType.Application.Zip.toString())

                        file.inputStream().use { inputStream ->
                            call.respondOutputStream(ContentType.Application.Zip) { inputStream.copyTo(this) }
                        }
                    } catch (e: IOException) {
                        call.respond(HttpStatusCode.InternalServerError, "Error reading zip file: ${e.message}")
                    }
                }

                get("/pluginzip") {
                    val folderToZip = ConfigManager.settings.pluginFolderToZip
                    if (folderToZip.isNullOrBlank()) {
                        call.respond(HttpStatusCode.InternalServerError, "Invalid plugin folder path in configuration.")
                        return@get
                    }

                    try {
                        val zipBytes = zipFolder(folderToZip)
                        call.response.header(HttpHeaders.ContentDisposition, "attachment; filename=\"pluginzip.zip\"")
                        call.respondBytes(zipBytes, ContentType.Application.Zip)
                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.InternalServerError, "Unexpected error: ${e.message}")
                    }
                }
            }
        }.start(wait = false)


        // Load configuration
        val settings = ConfigManager.settings
    }


    override fun onDisable() {
        logger.info("Disabling PackSender")
        ConfigManager.save()
    }


}