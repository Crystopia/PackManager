package net.crystopia.packsender.config

import kotlinx.serialization.Serializable

@Serializable
data class ConfigData(
    var resourcepackUrl: String?,
    val resourcepackHash: String?,
    val APIPort: Int? = 9900,
    val RPzipFilePath: String?,
    var pluginFolderToZip: String?,
    val message: String?
)
