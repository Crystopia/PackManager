package net.crystopia.packmanager.events

import com.nexomc.nexo.api.events.resourcepack.NexoPostPackGenerateEvent
import net.crystopia.packmanager.config.ConfigManager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import java.util.UUID

object NexoGeneratePack : Listener {

    @EventHandler
    fun onGenPackEvent(event: NexoPostPackGenerateEvent) {

        println("Generating pack...")

        val config = ConfigManager.settings.packMode
        val id = UUID.randomUUID().toString().split("-")[0]

        if (config != "Development") {
            val packMcMeta = """
            {
             "pack": {
             "pack_format": 15,
             "description": "v.${id}-dev"
             }
            }
            """.trimIndent().toByteArray(Charsets.UTF_8)

            event.addUnknownFile("pack.mcmeta", packMcMeta)


        } else if (config == "Production") {
            val packMcMeta = """
            {
             "pack": {
             "pack_format": 15,
             "description": "v.${id}"
             }
            }
            """.trimIndent().toByteArray(Charsets.UTF_8)

            event.addUnknownFile("pack.mcmeta", packMcMeta)
        } else {
            return
        }
    }
}