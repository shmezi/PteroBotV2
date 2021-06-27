package me.alexirivng.bot.commands;

import com.mattmalec.pterodactyl4j.client.entities.ClientServer;
import me.alexirivng.bot.utils.Utils;
import me.alexirivng.bot.utils.Messages;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import java.util.Objects;

import static me.alexirivng.bot.DiscordBot.memberServers;
import static me.alexirivng.bot.DiscordBot.panelSessionPageNumber;

public class Rename {
    public void renameCommand(SlashCommandEvent event){
        Member member = event.getMember();
        assert member != null;
        String memberId = member.getId();
        String guildId = Objects.requireNonNull(event.getGuild()).getId();
        ClientServer server = memberServers.get(guildId, memberId).get(panelSessionPageNumber.get(guildId, memberId));
        server.getManager().setName(Objects.requireNonNull(event.getOption("name")).getAsString()).executeAsync();
        event.getHook().editOriginal(Utils.getConfigMessage(guildId, Messages.RENAMED_SERVER)).queue();
    }
}
