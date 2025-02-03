package net.crystopia.packsender

import net.crystopia.packsender.config.ConfigManager
import net.kyori.adventure.resource.ResourcePackInfo
import net.kyori.adventure.resource.ResourcePackRequest
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerResourcePackStatusEvent
import org.bukkit.potion.PotionEffectType
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.net.URI

class PlayerJoinEvent : Listener {

    val mm = MiniMessage.miniMessage()

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player

        event.joinMessage(net.kyori.adventure.text.Component.text(""))

        val PACK_INFO = ResourcePackInfo.resourcePackInfo().uri(URI.create(ConfigManager.settings.resourcepackUrl!!))
            .hash(ConfigManager.settings.resourcepackHash.toString()).build();

        val request = ResourcePackRequest.resourcePackRequest().packs(PACK_INFO).prompt(
            mm.deserialize(
                ConfigManager.settings.message.toString(),
            )
        ).required(true).build();

        player.sendResourcePacks(request);
    }

    @EventHandler
    fun onPackStatus(event: PlayerResourcePackStatusEvent) {
        val player = event.player
        val status = event.status

        if (status == PlayerResourcePackStatusEvent.Status.DOWNLOADED) {
        } else if (status == PlayerResourcePackStatusEvent.Status.DECLINED) {
            player.kick();
        } else if (status == PlayerResourcePackStatusEvent.Status.FAILED_DOWNLOAD) {
            player.kick();
        }
    }
}
