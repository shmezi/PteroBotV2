package me.alexirivng.bot.events;

import me.alexirivng.bot.DiscordBot;
import me.alexirivng.bot.utils.Utils;
import me.alexirivng.bot.commands.Link;
import me.alexirivng.bot.commands.Rename;
import me.alexirivng.bot.commands.Send;
import me.alexirivng.bot.commands.Server;
import me.alexirivng.bot.utils.Messages;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.Button;

public class SlashCommand extends ListenerAdapter {

    public void onSlashCommand(SlashCommandEvent event) {
        event.deferReply(true).queue();
        if (event.isFromGuild()) {
            Member member = event.getMember();
            String memberId = member.getId();
            String guildId = event.getGuild().getId();
            switch (event.getName()) {
                case "link":
                    new Link().linkCommand(event);
                    break;
                case "s", "server", "servers":
                    new Server().serverCommand(event);

                    break;
                case "send":
                    new Send().sendCommand(event);
                    break;
                case "rename":
                    new Rename().renameCommand(event);
                    break;
                case "reinstall":
                    event.getHook().editOriginal(Utils.getConfigMessage(guildId, Messages.REINSTALL_VERIFY)).setActionRow(Button.danger(memberId + guildId + "m", "Yes")).queue();
                    break;

            }

        } else {
            event.getHook().editOriginal(DiscordBot.getGuildConfiguration.get("DMS", Messages.SEND_IN_SERVER)).queue();
        }

    }
}