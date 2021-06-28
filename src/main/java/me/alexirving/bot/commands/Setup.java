package me.alexirving.bot.commands;

import me.alexirving.bot.Database;
import me.alexirving.bot.ServerManager;
import me.alexirving.bot.utils.Messages;
import me.alexirving.bot.utils.Utils;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import java.io.IOException;


public class Setup {
    public void setupCommand(SlashCommandEvent event) throws IOException {
        String guildId = event.getGuild().getId();
        Member member = event.getMember();
        Guild guild = event.getGuild();
        if (member.hasPermission(Permission.ADMINISTRATOR)) {
            try {
                switch (event.getOption("value").getAsString()) {
                    case "API_URL", "PANEL_AVATAR_URL", "INFO_AVATAR_URL", "HELP_AVATAR_URL":
                        if (Utils.isLinkValid(event.getOption("to").getAsString())) {
                            Database.getGuildConfiguration.put(guildId, Messages.valueOf(event.getOption("value").getAsString()), event.getOption("to").getAsString());
                            Database.updateMessagesJson();
                            event.getHook().editOriginal("Configuration has been set!").queue();
                        }else{
                            event.getHook().editOriginal("Please send a valid link!").queue();
                        }
                        break;
                    default:
                        Database.getGuildConfiguration.put(guildId, Messages.valueOf(event.getOption("value").getAsString()), event.getOption("to").getAsString());
                        Database.updateMessagesJson();
                        event.getHook().editOriginal("Configuration has been set!").queue();
                        break;
                }

            } catch (IllegalArgumentException exception) {
                event.getHook().editOriginal("Invalid value to set.").queue();
            }

        } else {
            event.getHook().editOriginal(Database.getGuildConfiguration.get(guild, Messages.NO_PERMISSION)).queue();
        }
    }
}
