package me.alexirivng.bot.commands;

import com.mattmalec.pterodactyl4j.PteroBuilder;
import com.mattmalec.pterodactyl4j.client.entities.ClientServer;
import com.mattmalec.pterodactyl4j.client.entities.PteroClient;
import me.alexirivng.bot.DiscordBot;
import me.alexirivng.bot.utils.Utils;
import me.alexirivng.bot.utils.Messages;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import java.util.Objects;

import static me.alexirivng.bot.DiscordBot.memberServers;
import static me.alexirivng.bot.DiscordBot.panelSessionPageNumber;

public class Send {
    public void sendCommand(SlashCommandEvent event){
        Member member = event.getMember();
        assert member != null;
        String memberId = member.getId();
        String guildId = Objects.requireNonNull(event.getGuild()).getId();
        PteroClient client = PteroBuilder.createClient(Utils.getConfigMessage(guildId, Messages.API_URL), DiscordBot.currentToken.get(guildId, memberId));
        ClientServer server =  memberServers.get(guildId, memberId).get(panelSessionPageNumber.get(guildId, memberId));
        client.sendCommand(server, Objects.requireNonNull(event.getOption("command")).getAsString()).executeAsync();
        event.getHook().editOriginal(Utils.getConfigMessage(guildId, Messages.COMMAND_SENT)).queue();
    }
}
