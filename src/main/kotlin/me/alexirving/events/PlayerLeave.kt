package me.alexirving.events

import me.alexirving.groups.Manager
import me.alexirving.groups.Track
import me.alexirving.groups.updatePlayTime
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

class PlayerLeave : Listener {
    @EventHandler
    fun onLeave(e: PlayerQuitEvent) {
        for (track: Track in Manager.tracks) {
            updatePlayTime(track, e.player)
        }
        Manager.players.put(e.player,0)
    }
}