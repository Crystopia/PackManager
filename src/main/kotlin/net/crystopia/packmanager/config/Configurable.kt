package net.crystopia.packmanager.config

interface Configurable {

    fun save()
    fun load() {}
    fun reset() {}

}