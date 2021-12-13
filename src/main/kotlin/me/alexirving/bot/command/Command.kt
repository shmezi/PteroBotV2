package me.alexirving.bot.command

import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent


abstract class Command(
    val name: String,
    val description: String,
    val permission: Permission,
    vararg val args: Argument
) {

    abstract fun execute(event: SlashCommandEvent)

}