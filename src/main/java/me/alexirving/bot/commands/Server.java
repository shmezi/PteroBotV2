package me.alexirving.bot.commands;

import com.mattmalec.pterodactyl4j.PteroBuilder;
import me.alexirving.bot.ServerManager;
import me.alexirving.bot.utils.ButtonSet;
import me.alexirving.bot.utils.Messages;
import me.alexirving.bot.utils.Mode;
import me.alexirving.bot.utils.Utils;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import java.util.Objects;

import static me.alexirving.bot.Database.*;

public class Server {
    public void serverCommand(SlashCommandEvent event) {
        Member member = event.getMember();
        assert member != null;
        String memberId = member.getId();
        String guildId = Objects.requireNonNull(event.getGuild()).getId();
        if (Objects.isNull(event.getOption("user"))) {
            if (Utils.isLinked(member)) {

                currentToken.put(guildId, memberId, memberTokens.get(guildId, memberId));
                createPanel(event, member, memberId, guildId);

            } else {
                event.getHook().editOriginal(Utils.getConfigMessage(guildId, Messages.NOT_LINKED)).queue();

            }

        } else if (Utils.isStaff(member)) {
            Member mentioned = Objects.requireNonNull(event.getOption("user")).getAsMember();
            if (Utils.isLinked(mentioned)) {

                currentToken.put(guildId, memberId, memberTokens.get(guildId, mentioned.getId()));
                createPanel(event, member, memberId, guildId);
            } else {
                event.getHook().editOriginal(Utils.getConfigMessage(guildId, Messages.MENTIONED_NOT_LINKED)).queue();
            }

        } else {
            event.getHook().editOriginal(Utils.getConfigMessage(guildId, Messages.NOT_YOUR_ACCOUNT)).queue();
        }

    }

    private void createPanel(SlashCommandEvent event, Member member, String memberId, String guildId) {
        if (Utils.isLinkValid(Utils.getConfigMessage(guildId, Messages.API_URL))){
        if (!Objects.isNull(infoPanelSessionToDelete.get(guildId, memberId))) {
            infoPanelSessionToDelete.get(guildId, memberId).delete().queue();
        }
        if (!Objects.isNull(panelSessionToDelete.get(guildId, memberId))) {
            panelSessionToDelete.get(guildId, memberId).delete().queue();
        }
        panelSessionPageNumber.put(guildId, memberId, 0);

        memberPteroClients.put(guildId, memberId, PteroBuilder.createClient(Utils.getConfigMessage(guildId, Messages.API_URL), currentToken.get(guildId, memberId)));


        memberPteroClients.get(guildId, memberId).retrieveServers().executeAsync(
                clientServers -> {
                    if (clientServers.size() != 0) {
                        memberServers.put(guildId, memberId, clientServers);
                        event.getChannel().sendMessageEmbeds(ServerManager.updatePanel(member, Mode.UPDATE, memberPteroClients.get(guildId, memberId))).setActionRow(ServerManager.generateButtons(member, ButtonSet.NAVIGATE)).queue(message -> {
                            panelSessionToDelete.put(guildId, memberId, message);
                            panelSessions.put(guildId, memberId, message);
                        });
                        event.getChannel().sendMessageEmbeds(ServerManager.generateInfoPanel(member)).setActionRow(ServerManager.generateButtons(member, ButtonSet.ACTION)).queue(message -> {
                            infoPanelSessions.put(guildId, memberId, message);
                            infoPanelSessionToDelete.put(guildId, memberId, message);
                        });
                        event.getHook().editOriginal(Utils.getConfigMessage(guildId, Messages.PANEL_CREATED)).queue();
                    } else {
                        event.getHook().editOriginal(Utils.getConfigMessage(guildId, Messages.NO_SERVER)).queue();
                    }

                }
        );
    }else{
            event.getHook().editOriginal("API URL NOT SETUP CORRECTLY!").queue();
        }
}
}
