package me.alexirving.bot.commands;

import com.mattmalec.pterodactyl4j.client.entities.ClientServer;
import me.alexirving.bot.Database;
import me.alexirving.bot.utils.Messages;
import me.alexirving.bot.utils.Utils;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import java.util.Objects;


public class Rename {
    public void renameCommand(SlashCommandEvent event) {
        Member member = event.getMember();
        assert member != null;
        String memberId = member.getId();
        String guildId = Objects.requireNonNull(event.getGuild()).getId();
        if (!Objects.isNull(Database.memberServers.get(guildId, memberId)) || !Objects.isNull(Database.panelSessionPageNumber.get(guildId, memberId))) {
            ClientServer server = Database.memberServers.get(guildId, memberId).get(Database.panelSessionPageNumber.get(guildId, memberId));
            server.getManager().setName(Objects.requireNonNull(event.getOption("name")).getAsString()).executeAsync();
            event.getHook().editOriginal(Utils.getConfigMessage(guildId, Messages.RENAMED_SERVER)).queue();
        } else {
            event.getHook().editOriginal(Utils.getConfigMessage(guildId, Messages.NO_SELECTED_SERVER)).queue();
        }


    }
}
