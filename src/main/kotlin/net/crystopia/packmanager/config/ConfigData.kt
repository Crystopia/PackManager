package net.crystopia.packmanager.config

import kotlinx.serialization.Serializable

@Serializable
data class ConfigData(
    var resourcepackUrl: String?,
    val resourcepackHash: String?,
    val APIPort: Int? = 9900,
    var RPzipFilePath: String?,
    var pluginFolderToZip: String?,
    val message: String?,
    val packMode: String = "",
    val packVersion: MutableList<String> = mutableListOf("22","48"),
)
