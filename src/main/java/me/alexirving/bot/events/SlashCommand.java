package me.alexirving.bot.events;

import me.alexirving.bot.commands.*;
import me.alexirving.bot.utils.Messages;
import me.alexirving.bot.utils.Utils;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.Button;

import java.io.IOException;

public class SlashCommand extends ListenerAdapter {

    public void onSlashCommand(SlashCommandEvent event) {
        event.deferReply(true).queue();

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
                case "access":
                    new Access().accessCommand(event);
                    break;
                case "setup":
                    try {
                        new Setup().setupCommand(event);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case "help":
                    new Help().helpCommand(event);
                    break;
                case "staffhelp":
                    new StaffHelp().staffHelpCommand(event);
                    break;
                case "create":
                    new Create().createCommand(event);
                    break;
                case "invite","inv":
                    new Invite().inviteCommand(event);
                    break;
            }


    }
}