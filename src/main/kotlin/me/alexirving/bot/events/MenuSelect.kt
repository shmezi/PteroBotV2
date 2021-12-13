package me.alexirving.bot.events

import net.dv8tion.jda.api.events.interaction.SelectionMenuEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class MenuSelect : ListenerAdapter() {
    override fun onSelectionMenu(event: SelectionMenuEvent) {
        event.reply("COOL!").queue()
    }

}