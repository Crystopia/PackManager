package net.crystopia.packsender.config

interface Configurable {

    fun save()
    fun load() {}
    fun reset() {}

}