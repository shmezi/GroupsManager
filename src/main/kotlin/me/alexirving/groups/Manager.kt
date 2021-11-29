package me.alexirving.groups

import me.alexirving.getValue
import me.alexirving.setValue
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player

/**
 * Main manager for GroupManager
 */
class Manager {
    companion object {
        var players: HashMap<Player, Long> = HashMap()
        var tracks: ArrayList<Track> = ArrayList()
        var papi = false
    }


}


/**
 * Reloads all the tracks.
 * @param trackConfig The config file of the tracks.
 */
fun reloadTracks(trackConfig: FileConfiguration) {
    Manager.tracks.clear()
    for (track: String in trackConfig.getKeys(false)) {
        var tempG: ArrayList<Group> = ArrayList()
        for (group: String in trackConfig.getConfigurationSection(track).getKeys(false)) {
            tempG.add(
                Group(
                    group, trackConfig.getInt("${track}.${group}"),
                    getActions(0, trackConfig.getConfigurationSection("$track.$group")),
                    getActions(1, trackConfig.getConfigurationSection("$track.$group"))
                )
            )

        }
        Manager.tracks.add(Track(track, tempG))
    }
}

/**
 * Set the amount of time played for a player in the database.
 */
fun setPlayTime(track: Track, player: Player, time: Long) {
    setValue("GM_PLAYTIME", player, track.name, time.toString())
}

/**
 * Add an amount of time for a player in the database
 */
fun addToPlayTime(track: Track, player: Player, time: Long) {
    setPlayTime(track, player, getValue("GM_PLAYTIME", player, track.name).toLong())
}

/**
 * Update the play time for a player in the database.
 */
fun updatePlayTime(track: Track, player: Player) {
    addToPlayTime(track, player, System.currentTimeMillis() - Manager.players.get(player)!!)
}

fun promotePlayer(track: Track, player: Player) {
    track.getPlayerGroup(player).promote(player)
}

fun demotePlayer(track: Track, player: Player) {
    track.getPlayerGroup(player).demote(player)
}


fun getTrackFromName(name: String): Track {
    return Manager.tracks.filter { it.name == name }[0]
}


/**
 * A utility method to get the actions from a group's config section
 * @param type 0 - Gets the promotion actions. 1 - Gets the demotion actions. (Default - 1)
 * @param groupConfig The configuration section of the group.
 */
fun getActions(type: Int, groupConfig: ConfigurationSection): ArrayList<Action> {
    var actionRunner: String = ""
    actionRunner = when (type) {
        0 -> {
            "On-Promote"
        }
        1 -> {
            "On-Demote"
        }
        else -> {
            "On-Promote"
        }

    }
    var tempP: ArrayList<Action> = ArrayList()
    for (action: String in groupConfig.getConfigurationSection(actionRunner).getKeys(false)) {
        tempP.add(
            Action(
                ActionType.valueOf(groupConfig.getString("$actionRunner.$action")),
                groupConfig.getStringList("$actionRunner.$action")
            )
        )
    }
    return tempP
}