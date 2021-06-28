package me.alexirving.bot.commands;

import com.mattmalec.pterodactyl4j.PteroBuilder;
import com.mattmalec.pterodactyl4j.client.entities.ClientServer;
import com.mattmalec.pterodactyl4j.client.entities.PteroClient;
import me.alexirving.bot.Database;
import me.alexirving.bot.utils.Utils;
import me.alexirving.bot.utils.Messages;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import java.util.Objects;

import static me.alexirving.bot.Database.memberServers;
import static me.alexirving.bot.Database.panelSessionPageNumber;

public class Send {
    public void sendCommand(SlashCommandEvent event){
        Member member = event.getMember();
        assert member != null;
        String memberId = member.getId();
        String guildId = Objects.requireNonNull(event.getGuild()).getId();
        if (!Objects.isNull(Database.memberServers.get(guildId, memberId)) || !Objects.isNull(Database.panelSessionPageNumber.get(guildId, memberId))) {
            if (Utils.isLinkValid(Utils.getConfigMessage(guildId, Messages.API_URL))){
            PteroClient client = PteroBuilder.createClient(Utils.getConfigMessage(guildId, Messages.API_URL), Database.currentToken.get(guildId, memberId));
            ClientServer server =  memberServers.get(guildId, memberId).get(panelSessionPageNumber.get(guildId, memberId));
            client.sendCommand(server, Objects.requireNonNull(event.getOption("command")).getAsString()).executeAsync();
            event.getHook().editOriginal(Utils.getConfigMessage(guildId, Messages.COMMAND_SENT)).queue();
        }else{
                event.getHook().editOriginal("API URL NOT SETUP CORRECTLY!").queue();
            }
        }else{
            event.getHook().editOriginal(Utils.getConfigMessage(guildId, Messages.NO_SELECTED_SERVER)).queue();
        }


    }
}
