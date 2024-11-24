package net.crystopia.packsender.config

import kotlinx.serialization.encodeToString
import java.io.File

object ConfigManager {
    private val settingsFile = File("plugins/PackSender/config.json")


    val settings = settingsFile.loadConfig(ConfigData())


    fun save() {
        settingsFile.writeText(json.encodeToString(settings))
    }


}