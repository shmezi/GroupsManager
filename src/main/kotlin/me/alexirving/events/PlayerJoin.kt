package me.alexirving.events

import me.alexirving.addPlayerToTable
import me.alexirving.groups.Manager
import me.alexirving.isInTable
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class PlayerJoin : Listener {
    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        if (!isInTable("GM_TRACKS", e.player)) {
            addPlayerToTable("GM_PLAYTIME", e.player)
            addPlayerToTable("GM_TRACKS", e.player)
        }
        Manager.players.set(e.player, System.currentTimeMillis())
    }
}