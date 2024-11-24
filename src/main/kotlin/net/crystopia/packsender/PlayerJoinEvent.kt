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
    fun onPlayerMove(event: PlayerMoveEvent) {
        if (ConfigManager.settings.devmode == false) {
            event.isCancelled = true;
        }
    }

    @EventHandler
    fun onInventoryClickEvent(event: InventoryClickEvent) {
        if (ConfigManager.settings.devmode == false) {
            event.isCancelled = true;
        }
    }

    @EventHandler
    fun onInventoryOpenEvent(event: InventoryOpenEvent) {
        if (ConfigManager.settings.devmode == false) {
            event.isCancelled = true;
        }
    }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player

        val devmode: Boolean = false

        if (ConfigManager.settings.devmode == false) {

            event.joinMessage(net.kyori.adventure.text.Component.text(""))

            player.addPotionEffect(PotionEffectType.BLINDNESS.createEffect(999999999, 5))
            player.addPotionEffect(PotionEffectType.INVISIBILITY.createEffect(999999999, 5))

            event.player.teleport(Location(Bukkit.getWorld(player.world.name), 1000.0, 0.0, 1000.0))


            val PACK_INFO =
                ResourcePackInfo.resourcePackInfo().uri(URI.create("http://dedi1.server.nexocrew.space:2433/api/pack/apply"))
                    .hash("").build();

            val request = ResourcePackRequest.resourcePackRequest().packs(PACK_INFO).prompt(
                mm.deserialize(
                    "<strikethrough><grey>----------------------------------------<reset>\n" +
                            "<color:#75cfff>Hey " + player.name + "</color><dark_gray>,</dark_gray> \n<color:#ff5c7f>if you want to play on </color><color:#3bbeff>Crystopia </color><color:#ff5c7f>\nyou need a </color><color:#3dffc5>resourcepack</color><dark_gray>! </dark_gray>\n\n<color:#ffa640>Please accept it and start your adventure</color><dark_gray>.</dark_gray>\n<strikethrough><grey>----------------------------------------<reset>"
                )
            ).required(true).build();

            // Send the resource pack request to the target audience
            player.sendResourcePacks(request);
            player.gameMode = GameMode.SPECTATOR;

        } else if (ConfigManager.settings.devmode == true) {
            event.joinMessage(net.kyori.adventure.text.Component.text(""))

            val PACK_INFO =
                ResourcePackInfo.resourcePackInfo().uri(URI.create("http://dedi1.server.nexocrew.space:2433/api/pack/dev/apply"))
                    .hash("").build();

            val request = ResourcePackRequest.resourcePackRequest().packs(PACK_INFO).prompt(
                mm.deserialize(
                    "<strikethrough><grey>----------------------------------------<reset>\n\n<color:#75cfff>Hey " + player.name + "</color><dark_gray>,</dark_gray> \nThis is the Dev Mode please use the resourcepack.\n\n<color:#ffa640>Now accept it and start your work</color><dark_gray>.</dark_gray>\n<strikethrough><grey>----------------------------------------<reset>"
                )
            ).required(true).build();

            // Send the resource pack request to the target audience
            player.sendResourcePacks(request);

        }
    }

    @EventHandler
    fun onPackStatus(event: PlayerResourcePackStatusEvent) {
        val player = event.player
        val status = event.status

        if (status == PlayerResourcePackStatusEvent.Status.DOWNLOADED) {
            player.sendMessage(mm.deserialize("<color:#30ff83><bold>The Resourcepack is downloaded!</bold>\n<color:#52a8ff>Happy Play time!</color>"));
            if (ConfigManager.settings.devmode == false) {
                sendPlayerToServer(player, "Lobby-1")
            }
        } else if (status == PlayerResourcePackStatusEvent.Status.DECLINED) {
            player.kick(mm.deserialize("<color:#ff5d38><bold>Resourcepack declined!"));
        } else if (status == PlayerResourcePackStatusEvent.Status.FAILED_DOWNLOAD) {
            player.kick(mm.deserialize("<color:#ff5d38><bold>Resourcepack Failed!"));
        }
    }


    fun sendPlayerToServer(player: Player, serverName: String) {
        try {
            // Erstelle einen ByteArrayOutputStream und DataOutputStream
            val byteArrayOutputStream = ByteArrayOutputStream()
            val dataOutputStream = DataOutputStream(byteArrayOutputStream)

            // Schreibe die Daten in den Stream
            dataOutputStream.writeUTF("Connect")
            dataOutputStream.writeUTF(serverName)

            // Sende die Daten an BungeeCord
            player.sendPluginMessage(PackSender.instance, "BungeeCord", byteArrayOutputStream.toByteArray())

            // Schließe die Streams
            dataOutputStream.close()
            byteArrayOutputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}
