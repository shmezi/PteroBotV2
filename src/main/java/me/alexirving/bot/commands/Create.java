package me.alexirving.bot.commands;

import com.mattmalec.pterodactyl4j.PteroBuilder;
import com.mattmalec.pterodactyl4j.client.entities.PteroClient;
import me.alexirving.bot.Database;
import me.alexirving.bot.utils.Messages;
import me.alexirving.bot.utils.Utils;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public class Create {
    public void createCommand(SlashCommandEvent event) {
        Member member = event.getMember();
        String guildId = event.getGuild().getId();
        if (Utils.isLinked(member)) {
            if (Utils.isLinkValid(Utils.getConfigMessage(guildId, Messages.API_URL))){
            PteroClient client = PteroBuilder.createClient(Utils.getConfigMessage(guildId, Messages.API_URL), Database.currentToken.get(guildId, member.getId()));
            if (Utils.isStaff(member) && client.retrieveAccount().execute().isRootAdmin()) {

            } else {
                event.getHook().editOriginal(Utils.getConfigMessage(guildId, Messages.NO_PERMISSION)).queue();
            }

        }else{
                event.getHook().editOriginal("API URL NOT SETUP CORRECTLY!").queue();
            }
        } else {
            event.getHook().editOriginal(Utils.getConfigMessage(guildId, Messages.NOT_LINKED)).queue();
        }
    }
}
