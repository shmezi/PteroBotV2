package me.alexirving.bot.commands;

import me.alexirving.bot.utils.Messages;
import me.alexirving.bot.utils.Utils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public class StaffHelp {
    public void staffHelpCommand(SlashCommandEvent event) {
        String guildId = event.getGuild().getId();
        if (Utils.isStaff(event.getMember())) {

            EmbedBuilder builder = new EmbedBuilder();
            builder.setThumbnail(Utils.getConfigMessage(guildId, Messages.HELP_AVATAR_URL));
            builder.setTitle("PteroBot help");
            builder.addField("\u200E", "**Standard commands:**", false);
            builder.addField("> /link <API_KEY>", "• Links your discord account to ptero account.", false);
            builder.addField("> /server (/s, /servers)", "• Server control panel.", false);
            builder.addField("> /reinstall", "• Reinstall current selected server", false);
            builder.addField("> /rename", "• Rename current selected server", false);
            builder.addField("> /send <command> ", "• Send a command to the current selected server", false);
            builder.addField("\u200E", "**Staff commands:**", false);
            builder.addField("> /access", "• Add yourself as a sub-user to the current selected server.", false);
            builder.addField("> /create", "• Coming soon!", false);
            builder.addField("> /setup <type> <Value>", "• Configure the bot.", false);
            event.getHook().editOriginalEmbeds(builder.build()).queue();
        } else {
            event.getHook().editOriginal(Utils.getConfigMessage(guildId, Messages.NO_PERMISSION)).queue();
        }

    }
}
