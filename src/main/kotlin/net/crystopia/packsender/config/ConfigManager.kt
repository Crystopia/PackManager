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
            RPzipFilePath = "C:\\Users\\jespe\\Downloads\\pack.zip",
            message = "",
            pluginFolderToZip = ""
        )
    )


    fun save() {
        settingsFile.writeText(json.encodeToString(settings))
    }


}