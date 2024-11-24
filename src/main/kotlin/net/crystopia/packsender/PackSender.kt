package net.crystopia.packsender

import net.crystopia.packsender.config.ConfigManager
import org.bukkit.plugin.java.JavaPlugin
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

        // Load configuration
        val settings = ConfigManager.settings

    }


    override fun onDisable() {
        logger.info("Disabling PackSender")
        ConfigManager.save()
    }


}