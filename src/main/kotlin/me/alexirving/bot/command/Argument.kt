package me.alexirving.bot.command

import net.dv8tion.jda.api.interactions.commands.OptionType

class Argument(val type: OptionType,val name: String,val description: String,val required: Boolean)