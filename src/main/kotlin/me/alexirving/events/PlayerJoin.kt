package me.alexirving.events

import me.alexirving.groups.Manager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class PlayerJoin : Listener {
    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        Manager.players.set(e.player, System.currentTimeMillis())
    }
}