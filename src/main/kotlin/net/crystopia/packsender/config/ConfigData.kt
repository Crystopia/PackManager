package net.crystopia.packsender.config

import kotlinx.serialization.Serializable

@Serializable
data class ConfigData(var devmode: Boolean = true)
