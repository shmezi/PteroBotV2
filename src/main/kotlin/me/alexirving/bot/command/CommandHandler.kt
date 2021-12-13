package me.alexirving.bot.command

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.commands.build.CommandData

class CommandHandler : ListenerAdapter() {
    val commandMap = HashMap<String, Command>()
    override fun onSlashCommand(event: SlashCommandEvent) {
        if (commandMap.containsKey(event.name))
            commandMap[event.name]!!.execute(event)
        else
            event.reply("This command does not exist any longer or never did!").setEphemeral(true).queue()

    }


    fun registerCommands(vararg commands: Command) {
        for (command: Command in commands)
            commandMap[command.name] = command
    }

    fun getAsCommands(): List<CommandData> {
        val a = ArrayList<CommandData>()
        for (command: Command in commandMap.values) {
            val b = CommandData(command.name, command.description)
            for (argument: Argument in command.args)
                b.addOption(argument.type, argument.name, argument.description, argument.required)
            a.add(b)
        }
        return a
    }
}