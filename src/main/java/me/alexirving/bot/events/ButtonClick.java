package me.alexirving.bot.events;

import com.mattmalec.pterodactyl4j.PowerAction;
import com.mattmalec.pterodactyl4j.PteroBuilder;
import com.mattmalec.pterodactyl4j.client.entities.ClientServer;
import com.mattmalec.pterodactyl4j.client.entities.PteroClient;
import me.alexirving.bot.Database;
import me.alexirving.bot.ServerManager;
import me.alexirving.bot.utils.ButtonSet;
import me.alexirving.bot.utils.Messages;
import me.alexirving.bot.utils.Mode;
import me.alexirving.bot.utils.Utils;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Objects;


public class ButtonClick extends ListenerAdapter {
    @Override
    public void onButtonClick(ButtonClickEvent event) {

        String guildId = Objects.requireNonNull(event.getGuild()).getId();
        Member member = event.getGuild().retrieveMemberById(new StringBuffer(event.getComponentId()).deleteCharAt(event.getComponentId().length() - 1).toString()).complete();
        String memberId;
        if (Utils.isLinkValid(Utils.getConfigMessage(guildId, Messages.API_URL))) {
            if (!Objects.isNull(member)) {
                memberId = member.getId();
                if (!Objects.isNull(Database.currentToken.get(guildId, memberId))) {
                    if (event.getMember().getId().equals(memberId) || Utils.isStaff(event.getMember())) {
                        PteroClient pteroClient;
                        if (!event.isAcknowledged()) {
                            event.deferEdit().queue();
                        }
                        if (Objects.isNull(Database.memberPteroClients.get(guildId, memberId))) {
                            Database.memberPteroClients.put(guildId, memberId, PteroBuilder.createClient(Utils.getConfigMessage(member.getGuild().getId(), Messages.API_URL), Database.currentToken.get(guildId, memberId)));
                            pteroClient = Database.memberPteroClients.get(guildId, memberId);
                        } else {
                            pteroClient = Database.memberPteroClients.get(guildId, memberId);
                        }
                        String command = event.getComponentId().replace(memberId, "").replace(guildId, "");
                        switch (command) {
                            case "n" -> {
                                event.getHook().editOriginalEmbeds((ServerManager.updatePanel(member, Mode.NEXT, pteroClient))).setActionRow(ServerManager.generateButtons(member, ButtonSet.NAVIGATE)).queue();
                                Database.infoPanelSessions.get(guildId, memberId).editMessageEmbeds(ServerManager.generateInfoPanel(member)).setActionRow(ServerManager.generateButtons(member, ButtonSet.ACTION)).queue();
                            }
                            case "b" -> {
                                event.getHook().editOriginalEmbeds((ServerManager.updatePanel(member, Mode.PREVIOUS, pteroClient))).setActionRow(ServerManager.generateButtons(member, ButtonSet.NAVIGATE)).queue();
                                Database.infoPanelSessions.get(guildId, memberId).editMessageEmbeds(ServerManager.generateInfoPanel(member)).setActionRow(ServerManager.generateButtons(member, ButtonSet.ACTION)).queue();
                            }
                            case "s" -> {
                                ClientServer server = Database.memberServers.get(guildId, memberId).get(Database.panelSessionPageNumber.get(guildId, memberId));
                                pteroClient.setPower(server, PowerAction.START).executeAsync();
                                event.getHook().editOriginalEmbeds(ServerManager.generateInfoPanel(member)).setActionRow(ServerManager.generateButtons(member, ButtonSet.ACTION)).queue();
                                Database.panelSessions.get(guildId, memberId).editMessageEmbeds(ServerManager.updatePanel(member, Mode.UPDATE, pteroClient)).setActionRow(ServerManager.generateButtons(member, ButtonSet.NAVIGATE)).queue();
                            }
                            case "r" -> {
                                ClientServer server = Database.memberServers.get(guildId, memberId).get(Database.panelSessionPageNumber.get(guildId, memberId));
                                pteroClient.setPower(server, PowerAction.RESTART).executeAsync();
                                event.getHook().editOriginalEmbeds(ServerManager.generateInfoPanel(member)).setActionRow(ServerManager.generateButtons(member, ButtonSet.ACTION)).queue();
                                Database.panelSessions.get(guildId, memberId).editMessageEmbeds(ServerManager.updatePanel(member, Mode.UPDATE, pteroClient)).setActionRow(ServerManager.generateButtons(member, ButtonSet.NAVIGATE)).queue();
                            }
                            case "p" -> {
                                ClientServer server = Database.memberServers.get(guildId, memberId).get(Database.panelSessionPageNumber.get(guildId, memberId));
                                pteroClient.setPower(server, PowerAction.STOP).executeAsync();
                                event.getHook().editOriginalEmbeds(ServerManager.generateInfoPanel(member)).setActionRow(ServerManager.generateButtons(member, ButtonSet.KILL)).queue();
                                Database.panelSessions.get(guildId, memberId).editMessageEmbeds(ServerManager.updatePanel(member, Mode.UPDATE, pteroClient)).setActionRow(ServerManager.generateButtons(member, ButtonSet.NAVIGATE)).queue();
                            }
                            case "k" -> {
                                ClientServer server = Database.memberServers.get(guildId, memberId).get(Database.panelSessionPageNumber.get(guildId, memberId));
                                pteroClient.setPower(server, PowerAction.KILL).executeAsync();
                                event.getHook().editOriginalEmbeds(ServerManager.generateInfoPanel(member)).setActionRow(ServerManager.generateButtons(member, ButtonSet.ACTION)).queue();
                                Database.panelSessions.get(guildId, memberId).editMessageEmbeds(ServerManager.updatePanel(member, Mode.UPDATE, pteroClient)).setActionRow(ServerManager.generateButtons(member, ButtonSet.NAVIGATE)).queue();
                            }
                            case "f" -> {
                                event.getHook().editOriginalEmbeds((ServerManager.updatePanel(member, Mode.UPDATE, pteroClient))).setActionRow(ServerManager.generateButtons(member, ButtonSet.NAVIGATE)).queue();
                                Database.infoPanelSessions.get(guildId, memberId).editMessageEmbeds(ServerManager.generateInfoPanel(member)).setActionRow(ServerManager.generateButtons(member, ButtonSet.ACTION)).queue();
                            }
                            case "m" -> {
                                ClientServer server = Database.memberServers.get(guildId, memberId).get(Database.panelSessionPageNumber.get(guildId, memberId));
                                server.getManager().reinstall().executeAsync();
                                event.getHook().editOriginal(Utils.getConfigMessage(guildId, Messages.REINSTALL)).queue();
                            }
                        }

                    } else {
                        event.reply(Utils.getConfigMessage(guildId, Messages.NOT_YOUR_ACCOUNT)).setEphemeral(true).queue();
                    }
                } else {
                    Objects.requireNonNull(event.getMessage()).delete().queue();

                }
            } else {
                Objects.requireNonNull(event.getMessage()).delete().queue();

            }

        }else{
            event.getHook().editOriginal("API URL NOT SETUP CORRECTLY!").queue();
        }

    }
}