package net.crystopia.packsender.config

import kotlinx.serialization.encodeToString
import java.io.File

object ConfigManager {
    private val settingsFile = File("plugins/PackManager/config.json")


    val settings = settingsFile.loadConfig(
        ConfigData(
            APIPort = 9900,
            resourcepackUrl = "",
            resourcepackHash = "",
            zipFileDir = "C:\\Users\\jespe\\Downloads\\pack.zip", message = ""
        )
    )


    fun save() {
        settingsFile.writeText(json.encodeToString(settings))
    }


}