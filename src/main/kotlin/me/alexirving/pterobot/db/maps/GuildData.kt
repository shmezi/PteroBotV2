package me.alexirving.pterobot.db.maps

data class GuildData(
    val guildId: String,
    val settings: MutableMap<GuildSetting, String>
) {

}