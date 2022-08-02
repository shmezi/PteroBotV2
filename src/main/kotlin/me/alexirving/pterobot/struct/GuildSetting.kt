package me.alexirving.pterobot.struct


enum class GuildSetting(val type: SettingType) {
    URL(SettingType.STRING),
    API(SettingType.STRING),
    LINKED(SettingType.STRING),
    PANEL_TEMPLATE(SettingType.EMBED),
    STAFF(SettingType.STRING)
}