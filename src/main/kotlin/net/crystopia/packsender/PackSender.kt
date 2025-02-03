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
                    val file = File(ConfigManager.settings.zipFileDir!!)
                    if (file.exists()) {
                        call.respondFile(file)
                    } else {
                        call.respond(HttpStatusCode.NotFound, "404 - no File!")
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