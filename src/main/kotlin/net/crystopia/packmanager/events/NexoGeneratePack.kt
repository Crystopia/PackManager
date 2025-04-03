package net.crystopia.packmanager.events

import com.nexomc.nexo.api.events.resourcepack.NexoPostPackGenerateEvent
import com.nexomc.nexo.api.events.resourcepack.NexoPrePackGenerateEvent
import net.crystopia.packmanager.config.ConfigManager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import java.io.File
import java.util.UUID

object NexoGeneratePack : Listener {

    @EventHandler
    fun onGenPackEvent(event: NexoPrePackGenerateEvent) {

        println("[PackManager] Loading Extra Content for Crystopia")
        println("[PackManager] Loading Versioning...")

        val config = ConfigManager.settings.packMode
        val packVersions = ConfigManager.settings.packVersion
        val id = UUID.randomUUID().toString().split("-")[0]


        if (config == "Development") {
            println("[PackManager] Development mode generated.")
            val packMcMeta = """
        {"pack":{"pack_format":46,"description":"§bCrystopia Server Pack\n§7 Verion: §e${id}-dev","supported_formats":${packVersions.toString()}},"overlays":{"entries":[]},"sodium":{"ignored_shaders":[]}}
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
        {"pack":{"pack_format":46,"description":"§bCrystopia Server Pack\n§7 Verion: §e${id}","supported_formats":${packVersions.toString()}},"overlays":{"entries":[]},"sodium":{"ignored_shaders":[]}}
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