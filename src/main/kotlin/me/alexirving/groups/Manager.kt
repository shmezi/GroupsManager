package me.alexirving.groups

import me.alexirving.actions.Action
import me.alexirving.addColumn
import me.alexirving.getValue
import me.alexirving.setValue
import me.alexirving.utils.LogType
import me.alexirving.utils.compileAction
import me.alexirving.utils.formatMessage
import me.alexirving.utils.log
import net.milkbowl.vault.economy.Economy
import org.bukkit.ChatColor
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.Player
import java.io.File
import java.util.*

/**
 * Main manager for GroupManager
 */
class Manager {
    companion object {
        var players: HashMap<Player, Long> = HashMap()
        var tracks: ArrayList<Track> = ArrayList()
        var papi = false
        var econ: Economy? = null
        var lang: Properties = Properties()
    }

}

fun loadLang(langFile: File) {
    Manager.lang.load(langFile.inputStream())
}

/**
 * Get a definition from the lang file and format it.
 * @param name Name of property in lang file
 * @param player A player, if needed that will be used for replacing placeholders (will only actually use first player given.)
 */
fun getDefFormat(name: String, vararg player: Player): String {
    return if (player.isEmpty())
        ChatColor.translateAlternateColorCodes('&', Manager.lang.getProperty(name))
    else
        formatMessage(player[0], Manager.lang.getProperty(name))
}

/**
 * Reloads all the tracks.
 * @param tracksConfig The config file of the tracks.
 */
fun reloadTracks(tracksConfig: ConfigurationSection) {
    Manager.tracks.clear()
    for (track: String in tracksConfig.getKeys(false)) {
        val tempG: ArrayList<Group> = ArrayList()
        val groups = tracksConfig.getConfigurationSection(track).getKeys(false)
        for (group: String in groups) {
            tempG.add(
                Group(
                    group,
                    tracksConfig.getInt("${track}.${group}.Price"),
                    tracksConfig.getLong("${track}.${group}.Time"),
                    compileActions(tracksConfig.getStringList("$track.$group.On-Promote")),
                    compileActions(tracksConfig.getStringList("$track.$group.On-Demote"))
                )
            )

        }
        Manager.tracks.add(Track(track, tempG))
        addColumn("GM_PLAYTIME", track, 0)
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
 * Gets the current time passed for this user.
 */
fun getTime(track: Track, player: Player): Int {
    return getValue("GM_PLAYTIME", player, track.name).toInt()
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
fun compileActions(actions: List<String>?): ArrayList<Action> {
    val tempP: ArrayList<Action> = ArrayList()
    if (actions == null)
        return tempP
    for (action: String in actions) {
        if (compileAction(action) == null)
            log(LogType.ERROR, "The following action has bad syntax! '$action'")
        else
            tempP.add(compileAction(action)!!)
    }
    return tempP
}