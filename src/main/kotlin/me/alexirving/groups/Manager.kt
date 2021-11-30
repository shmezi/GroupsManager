package me.alexirving.groups

import me.alexirving.addColumn
import me.alexirving.getValue
import me.alexirving.setValue
import org.bukkit.configuration.ConfigurationSection
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
fun reloadTracks(trackConfig: ConfigurationSection) {
    Manager.tracks.clear()
    for (track: String in trackConfig.getKeys(false)) {
        val tempG: ArrayList<Group> = ArrayList()
        val groups = trackConfig.getConfigurationSection(track).getKeys(false)
        for (group: String in groups) {
            tempG.add(
                Group(
                    group, trackConfig.getInt("${track}.${group}"),
                    getActions(0, trackConfig.getConfigurationSection("$track.$group")),
                    getActions(1, trackConfig.getConfigurationSection("$track.$group"))
                )
            )

        }
        Manager.tracks.add(Track(track, tempG))
        addColumn("GM_PLAYTIME", track, groups.first())
        addColumn("GM_TRACKS", track, groups.first())

    }
}

/**
 * Set the amount of time played for a player in the database.
 */
fun setPlayTime(track: Track, player: Player, time: Long) {
    setValue("GM_PLAYTIME", player, track.name, time.toString())
}

/**
 * Add an amount of time for a player in the database (MS)
 */
fun addToPlayTime(track: Track, player: Player, time: Long) {
    setPlayTime(track, player, getValue("GM_PLAYTIME", player, track.name).toLong() + time)
}

/**
 * Remove an amount of time for a player in the database (MS)
 */
fun subtractFromTime(track: Track, player: Player, time: Long) {
    setPlayTime(track, player, getValue("GM_PLAYTIME", player, track.name).toLong() - time)
}

/**
 * Update the play time for a player in the database.
 */
fun updatePlayTime(track: Track, player: Player) {
    addToPlayTime(track, player, System.currentTimeMillis() - Manager.players.get(player)!!)
}

/**
 * Gets a track from a string (returns null if not found)
 */
fun getTrackFromName(name: String): Track? {
    return if (Manager.tracks.filter { it.name == name }.size <= 0)
        null
    else
        Manager.tracks.filter { it.name == name }[0]
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
    if (groupConfig.getConfigurationSection(actionRunner) == null)
        return tempP
    for (action: String in groupConfig.getConfigurationSection(actionRunner).getKeys(false)) {
        tempP.add(
            Action(
                ActionType.valueOf(action.uppercase()),
                groupConfig.getStringList("$actionRunner.$action")
            )
        )
    }
    return tempP
}