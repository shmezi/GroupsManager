package me.alexirving.events

import me.alexirving.addPlayerToTable
import me.alexirving.groups.*
import me.alexirving.isInTable
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class PlayerJoin : Listener {
    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        val player = e.player
        if (!isInTable("GM_TRACKS", player)) {
            addPlayerToTable("GM_PLAYTIME", player)
            addPlayerToTable("GM_TRACKS", player)
        }
        Manager.players.set(player, System.currentTimeMillis())
        for (track: Track in Manager.tracks) {
            updatePlayTime(track, player)
            Manager.players.put(player, 0)
            val g = track.getNextGroup(track.getPlayerGroup(player))
            if (g != null)
                if (getTime(track, player) >= g.time) {
                    track.promote(player)
                    subtractFromTime(track, player,g.time)
                }
        }
    }
}