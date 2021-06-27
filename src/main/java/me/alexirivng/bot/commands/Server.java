package me.alexirivng.bot.commands;

import com.mattmalec.pterodactyl4j.PteroBuilder;
import com.mattmalec.pterodactyl4j.client.entities.PteroClient;
import me.alexirivng.bot.ServerManager;
import me.alexirivng.bot.utils.ButtonSet;
import me.alexirivng.bot.utils.Messages;
import me.alexirivng.bot.utils.Mode;
import me.alexirivng.bot.utils.Utils;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import java.util.Objects;

import static me.alexirivng.bot.DiscordBot.*;

public class Server {
    public void serverCommand(SlashCommandEvent event) {
        Member member = event.getMember();
        assert member != null;
        String memberId = member.getId();
        String guildId = Objects.requireNonNull(event.getGuild()).getId();
        if (Objects.isNull(event.getOption("user"))) {
            if (Utils.isLinked(member)) {
                if (!Objects.isNull(infoPanelSessionToDelete.get(guildId, memberId))) {
                    infoPanelSessionToDelete.get(guildId, memberId).delete().queue();
                }
                if (!Objects.isNull(panelSessionToDelete.get(guildId, memberId))) {
                    panelSessionToDelete.get(guildId, memberId).delete().queue();
                }
                currentToken.put(guildId, memberId, memberTokens.get(guildId, memberId));
                createPanel(event, member, memberId, guildId);

            } else {
                event.getHook().editOriginal(Utils.getConfigMessage(guildId, Messages.NOT_LINKED)).queue();

            }

        } else if (!Objects.isNull(event.getGuild().getRoleById(Utils.getConfigMessage(guildId, Messages.STAFF_ID)))) {
            Member mentioned = Objects.requireNonNull(event.getOption("user")).getAsMember();
            if (Utils.isLinked(mentioned)) {
                if (!Objects.isNull(infoPanelSessionToDelete.get(guildId, memberId))) {
                    infoPanelSessionToDelete.get(guildId, memberId).delete().queue();
                }
                if (!Objects.isNull(panelSessionToDelete.get(guildId, memberId))) {
                    panelSessionToDelete.get(guildId, memberId).delete().queue();
                }
                currentToken.put(guildId, memberId, memberTokens.get(guildId, mentioned.getId()));
                createPanel(event, member, memberId, guildId);


            } else {
                event.getHook().editOriginal(Utils.getConfigMessage(guildId, Messages.MENTIONED_NOT_LINKED)).queue();
            }

        } else {
            event.getHook().editOriginal("Please make sure that you have set up Staff role right!").queue();
        }

    }

    private void createPanel(SlashCommandEvent event, Member member, String memberId, String guildId) {
        panelSessionPageNumber.put(guildId, memberId, 0);
        PteroClient pteroClient;
        if (Objects.isNull(memberPteroClients.get(guildId, memberId))) {
            memberPteroClients.put(guildId, memberId, PteroBuilder.createClient(Utils.getConfigMessage(guildId, Messages.API_URL), currentToken.get(guildId, memberId)));
            pteroClient = memberPteroClients.get(guildId, memberId);
        } else {

            pteroClient = memberPteroClients.get(guildId, memberId);
        }
        pteroClient.retrieveServers().executeAsync(
                clientServers -> {
                    if (clientServers.size() != 0) {
                        memberServers.put(guildId, memberId, clientServers);
                        event.getChannel().sendMessageEmbeds(ServerManager.updatePanel(member, Mode.UPDATE, pteroClient)).setActionRow(ServerManager.generateButtons(member, ButtonSet.NAVIGATE)).queue(message -> {
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
    }
}
