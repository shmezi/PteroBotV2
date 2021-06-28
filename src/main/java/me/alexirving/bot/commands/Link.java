package me.alexirving.bot.commands;

import com.mattmalec.pterodactyl4j.PteroBuilder;
import com.mattmalec.pterodactyl4j.client.entities.PteroClient;
import me.alexirving.bot.Database;
import me.alexirving.bot.ServerManager;
import me.alexirving.bot.utils.Utils;
import me.alexirving.bot.utils.Messages;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import java.io.IOException;
import java.util.Objects;

import static me.alexirving.bot.Database.memberPteroClients;
import static me.alexirving.bot.Database.memberTokens;
import static me.alexirving.bot.utils.Utils.updateActivity;

public class Link {
    public void linkCommand(SlashCommandEvent event){
        Member member = event.getMember();
        assert member != null;
        String memberId = member.getId();
        String guildId = Objects.requireNonNull(event.getGuild()).getId();
        if (Utils.isLinkValid(Utils.getConfigMessage(guildId, Messages.API_URL))){
        PteroClient pteroClientLink = PteroBuilder.createClient(Utils.getConfigMessage(guildId, Messages.API_URL), Objects.requireNonNull(event.getOption("key")).getAsString());
        memberPteroClients.put(guildId, memberId, pteroClientLink);
        pteroClientLink.retrieveAccount().executeAsync(ac -> {
            memberTokens.put(guildId, memberId, Objects.requireNonNull(event.getOption("key")).getAsString());
            try {
                Database.updateTokensJson();
            } catch (IOException e) {
                e.printStackTrace();
            }
            updateActivity(event.getJDA());
            event.getHook().editOriginal(Utils.getConfigMessage(guildId, Messages.LINKED)).queue();


        }, throwable -> event.getHook().editOriginal(Utils.getConfigMessage(guildId, Messages.INVALID_APIKEY)).queue());
    }else{
            event.getHook().editOriginal("API URL NOT SETUP CORRECTLY!").queue();
        }
    }
}
