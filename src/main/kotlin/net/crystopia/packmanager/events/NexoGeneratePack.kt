package net.crystopia.packmanager.events

import com.nexomc.nexo.api.events.resourcepack.NexoPostPackGenerateEvent
import net.crystopia.packmanager.config.ConfigManager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import java.io.File
import java.util.UUID

object NexoGeneratePack : Listener {

    @EventHandler
    fun onGenPackEvent(event: NexoPostPackGenerateEvent) {

        println("[PackManager] Loading Extra Content for Crystopia")
        println("[PackManager] Loading Versioning...")

        val config = ConfigManager.settings.packMode
        val id = UUID.randomUUID().toString().split("-")[0]

        if (config == "Development") {
            println("[PackManager] Development mode generated.")
            val packMcMeta = """
        {
         "pack": {
         "pack_format": 15,
         "description": "v.${id}-dev"
         }
        }
    """.trimIndent().toByteArray(Charsets.UTF_8)

            val packDir = File("plugins/Nexo/pack")
            if (!packDir.exists()) {
                packDir.mkdirs()
            }


            val newFile = File(packDir, "pack.mcmeta")
            newFile.writeBytes(packMcMeta)

        } else if (config == "Production") {
            println("[PackManager] Production mode generated.")

            val packMcMeta = """
        {
         "pack": {
         "pack_format": 15,
         "description": "v.${id}"
         }
        }
    """.trimIndent().toByteArray(Charsets.UTF_8)


            val packDir = File("plugins/Nexo/pack")
            if (!packDir.exists()) {
                packDir.mkdirs()
            }

            val newFile = File(packDir, "pack.mcmeta")
            newFile.writeBytes(packMcMeta)
        } else {
            return
        }


        println("[PackManager] Generating and patching done.")
    }
}