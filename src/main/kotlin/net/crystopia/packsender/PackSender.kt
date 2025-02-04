package net.crystopia.packsender

import io.ktor.server.application.*
import net.crystopia.packsender.config.ConfigManager
import org.bukkit.plugin.java.JavaPlugin
import io.ktor.http.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File
import kotlin.io.path.Path
import kotlin.io.path.deleteExisting
import kotlin.io.path.exists

class PackSender : JavaPlugin() {

    companion object {
        lateinit var instance: PackSender
    }

    init {
        instance = this
    }

    override fun onEnable() {
        logger.info("Enabling PackSender")
        server.pluginManager.registerEvents(PlayerJoinEvent(), this)
        server.messenger.registerOutgoingPluginChannel(this, "BungeeCord")

        // Start Web API
        embeddedServer(Netty, port = ConfigManager.settings.APIPort!!.toInt()) {
            routing {
                get("/") {
                    val file = File(ConfigManager.settings.RPzipFilePath!!)
                    if (file.exists()) {
                        call.respondFile(file)
                    } else {
                        call.respond(HttpStatusCode.NotFound, "404 - no File!")
                    }
                }
                get("/pluginzip") {
                    try {
                        val folderToZip = ConfigManager.settings.pluginFolderToZip.toString()
                        val zipFilePath = "$folderToZip.zip"
                        zipFolder(folderToZip, zipFilePath)
                        call.respondFile(File(zipFilePath))

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