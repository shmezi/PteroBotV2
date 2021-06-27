package me.alexirivng.bot.commands;

import com.mattmalec.pterodactyl4j.PteroBuilder;
import com.mattmalec.pterodactyl4j.client.entities.PteroClient;
import me.alexirivng.bot.ServerManager;
import me.alexirivng.bot.utils.Utils;
import me.alexirivng.bot.utils.Messages;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import java.io.IOException;
import java.util.Objects;

import static me.alexirivng.bot.DiscordBot.memberPteroClients;
import static me.alexirivng.bot.DiscordBot.memberTokens;
import static me.alexirivng.bot.utils.Utils.updateActivity;

public class Link {
    public void linkCommand(SlashCommandEvent event){
        Member member = event.getMember();
        assert member != null;
        String memberId = member.getId();
        String guildId = Objects.requireNonNull(event.getGuild()).getId();
        PteroClient pteroClientLink = PteroBuilder.createClient(Utils.getConfigMessage(guildId, Messages.API_URL), Objects.requireNonNull(event.getOption("key")).getAsString());
        memberPteroClients.put(guildId, memberId, pteroClientLink);
        pteroClientLink.retrieveAccount().executeAsync(ac -> {
            memberTokens.put(guildId, memberId, Objects.requireNonNull(event.getOption("key")).getAsString());
            try {
                ServerManager.updateJsons();
            } catch (IOException e) {
                e.printStackTrace();
            }
            updateActivity(event.getJDA());
            event.getHook().editOriginal(Utils.getConfigMessage(guildId, Messages.LINKED)).queue();


        }, throwable -> event.getHook().editOriginal(Utils.getConfigMessage(guildId, Messages.INVALID_APIKEY)).queue());
    }
}
