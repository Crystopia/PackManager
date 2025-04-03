package net.crystopia.packmanager.events

import com.nexomc.nexo.api.events.resourcepack.NexoPackUploadEvent
import com.nexomc.nexo.api.events.resourcepack.NexoPostPackGenerateEvent
import com.nexomc.nexo.api.events.resourcepack.NexoPrePackGenerateEvent
import kotlinx.coroutines.awaitAll
import net.crystopia.packmanager.config.ConfigManager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import java.io.File
import java.util.UUID

object NexoGeneratePack : Listener {

    @EventHandler
    fun onGenPackEvent(event: NexoPrePackGenerateEvent) {

        val file = File("plugins/Nexo/pack/packsquash/.cache")
        deleteFolder(file)
        file.mkdirs()

        println("[PackManager] Loading Extra Content for Crystopia")
        println("[PackManager] Loading Versioning...")

        val config = ConfigManager.settings.packMode
        val packVersions = ConfigManager.settings.packVersion
        val id = UUID.randomUUID().toString().split("-")[0]


        if (config == "Development") {
            println("[PackManager] Development mode generated.")
            val packMcMeta = """
        {"pack":{"pack_format":46,"description":"§bCrystopia Server Pack\n§7 Version: §e${id}-dev","supported_formats":${packVersions.toString()}},"overlays":{"entries":[]},"sodium":{"ignored_shaders":[]}}
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
        {"pack":{"pack_format":46,"description":"§bCrystopia Server Pack\n§7 Version: §e${id}","supported_formats":${packVersions.toString()}},"overlays":{"entries":[]},"sodium":{"ignored_shaders":[]}}
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

    @EventHandler
    fun onUpload(event: NexoPackUploadEvent) {

        val folder = File("plugins/Nexo/pack/packsquash/.cache")
        if (!folder.exists()) {
            return
        } else {
            if (folder.isDirectory) {
                folder.listFiles()?.forEach {
                    println("[PackManager] Getting ${it.name}")
                    println("[PackManager] Set File to Resource-pack Download API")
                    ConfigManager.settings.RPzipFilePath = it.absolutePath
                }
            }
        }

    }

    private fun deleteFolder(folder: File): Boolean {
        if (folder.exists()) {
            folder.listFiles()?.forEach { file ->
                if (file.isDirectory) {
                    deleteFolder(file)
                } else {
                    file.delete()
                }
            }
        }
        return folder.delete()
    }

}

