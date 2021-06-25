package me.alexirivng.bot.events;

import com.mattmalec.pterodactyl4j.PteroBuilder;
import com.mattmalec.pterodactyl4j.client.entities.ClientServer;
import com.mattmalec.pterodactyl4j.client.entities.PteroClient;
import me.alexirivng.bot.DiscordBot;
import me.alexirivng.bot.ServerManager;
import me.alexirivng.bot.Utils;
import me.alexirivng.bot.utils.ButtonSet;
import me.alexirivng.bot.utils.Messages;
import me.alexirivng.bot.utils.Mode;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.Button;

import java.io.IOException;
import java.util.Objects;

import static me.alexirivng.bot.DiscordBot.*;
import static me.alexirivng.bot.Utils.updateActivity;

public class SlashCommand extends ListenerAdapter {

    public void onSlashCommand(SlashCommandEvent event) {
        event.deferReply(true).queue();
        if (event.isFromGuild()) {
            Member member = event.getMember();
            switch (event.getName()) {
                case "link":
                    PteroClient pteroClientLink = PteroBuilder.createClient(Utils.getConfigMessage(member.getGuild().getId(), Messages.API_URL), event.getOption("key").getAsString());
                    memberPteroClients.put(event.getMember().getId(), event.getGuild().getId(), pteroClientLink);
                    pteroClientLink.retrieveAccount().executeAsync(ac -> {
                        memberTokens.put(event.getMember().getId(), event.getGuild().getId(), event.getOption("key").getAsString());
                        try {
                            ServerManager.updateJsons();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        updateActivity(event.getJDA());
                        event.getHook().editOriginal(Utils.getConfigMessage(event.getGuild().getId(), Messages.LINKED)).queue();


                    }, throwable -> {
                        event.getHook().editOriginal(Utils.getConfigMessage(event.getGuild().getId(), Messages.INVALID_APIKEY)).queue();

                    });
                    break;
                case "s", "server", "servers":
                    PteroClient pteroClient;
                    if (Objects.isNull(event.getOption("user"))) {
                        if (Utils.isLinked(member)) {
                            if (!Objects.isNull(infoPanelSessionToDelete.get(member.getId(), event.getGuild().getId()))) {
                                infoPanelSessionToDelete.get(member.getId(), event.getGuild().getId()).delete().queue();
                            }
                            if (!Objects.isNull(panelSessionToDelete.get(member.getId(), event.getGuild().getId()))) {
                                panelSessionToDelete.get(member.getId(), event.getGuild().getId()).delete().queue();
                            }
                            currentToken.put(member.getId(), event.getGuild().getId(), memberTokens.get(member.getId(), event.getGuild().getId()));
                            panelSessionPageNumber.put(member.getId(), event.getGuild().getId(), 0);
                            pteroClient = PteroBuilder.createClient(Utils.getConfigMessage(member.getGuild().getId(), Messages.API_URL), currentToken.get(member.getId(), event.getGuild().getId()));
                            memberServers.put(member.getId(), event.getGuild().getId(), pteroClient.retrieveServers().execute());
                            event.getChannel().sendMessageEmbeds(ServerManager.updatePanel(member, Mode.UPDATE, pteroClient)).setActionRow(ServerManager.generateButtons(member, ButtonSet.NAVIGATE)).queue(message -> {
                                panelSessionToDelete.put(member.getId(), event.getGuild().getId(), message);
                                panelSessions.put(member.getId(), event.getGuild().getId(), message);
                            });
                            event.getChannel().sendMessageEmbeds(ServerManager.generateInfoPanel(member)).setActionRow(ServerManager.generateButtons(member, ButtonSet.ACTION)).queue(message -> {
                                infoPanelSessions.put(member.getId(), event.getGuild().getId(), message);
                                infoPanelSessionToDelete.put(member.getId(), event.getGuild().getId(), message);

                            });
                            event.getHook().editOriginal(Utils.getConfigMessage(event.getGuild().getId(), Messages.PANEL_CREATED)).queue();
                        } else {
                            event.getHook().editOriginal(Utils.getConfigMessage(event.getGuild().getId(), Messages.NOT_LINKED)).queue();

                        }


                    } else if (Objects.isNull(event.getGuild().getRoleById(DiscordBot.getGuildConfiguration.get(event.getGuild().getId(), Messages.STAFF_ID)))){
                        System.out.println("ERROR! STAFF ROLE HAS NOT BEEN SET CORRECTLY!");
                    }else if
                    (member.getRoles().contains(event.getGuild().getRoleById(DiscordBot.getGuildConfiguration.get(event.getGuild().getId(), Messages.STAFF_ID)))) {
                        Member mentioned = event.getOption("user").getAsMember();
                        if (Utils.isLinked(mentioned)) {
                            if (!Objects.isNull(DiscordBot.infoPanelSessionToDelete.get(member.getId(), event.getGuild().getId()))) {
                                DiscordBot.infoPanelSessionToDelete.get(member.getId(), event.getGuild().getId()).delete().queue();
                            }
                            if (!Objects.isNull(DiscordBot.panelSessionToDelete.get(member.getId(), event.getGuild().getId()))) {
                                DiscordBot.panelSessionToDelete.get(member.getId(), event.getGuild().getId()).delete().queue();
                            }
                            DiscordBot.currentToken.put(member.getId(), event.getGuild().getId(), DiscordBot.memberTokens.get(mentioned.getId(), event.getGuild().getId()));
                            DiscordBot.panelSessionPageNumber.put(member.getId(), event.getGuild().getId(), 0);
                            pteroClient = PteroBuilder.createClient(Utils.getConfigMessage(member.getGuild().getId(), Messages.API_URL), DiscordBot.currentToken.get(member.getId(), event.getGuild().getId()));
                            DiscordBot.memberServers.put(member.getId(), event.getGuild().getId(), pteroClient.retrieveServers().execute());
                            event.getChannel().sendMessageEmbeds(ServerManager.updatePanel(member, Mode.UPDATE, pteroClient)).setActionRow(ServerManager.generateButtons(member, ButtonSet.NAVIGATE)).queue(message -> {
                                DiscordBot.panelSessionToDelete.put(member.getId(), event.getGuild().getId(), message);
                                DiscordBot.panelSessions.put(member.getId(), event.getGuild().getId(), message);
                            });
                            event.getChannel().sendMessageEmbeds(ServerManager.generateInfoPanel(member)).setActionRow(ServerManager.generateButtons(member, ButtonSet.ACTION)).queue(message -> {
                                DiscordBot.infoPanelSessions.put(member.getId(), event.getGuild().getId(), message);
                                DiscordBot.infoPanelSessionToDelete.put(member.getId(), event.getGuild().getId(), message);
                            });
                            event.getHook().editOriginal(Utils.getConfigMessage(event.getGuild().getId(), Messages.PANEL_CREATED)).queue();
                        } else {
                            event.getHook().editOriginal(Utils.getConfigMessage(event.getGuild().getId(), Messages.MENTIONED_NOT_LINKED)).queue();
                        }
                    } else {
                        event.getHook().editOriginal(Utils.getConfigMessage(event.getGuild().getId(), Messages.NOT_YOUR_ACCOUNT)).queue();
                    }
                    break;
                case "send":
                    PteroClient client = PteroBuilder.createClient(Utils.getConfigMessage(member.getGuild().getId(), Messages.API_URL), DiscordBot.currentToken.get(member.getId(), event.getGuild().getId()));
                    ClientServer server = (ClientServer) memberServers.get(member.getId(), event.getGuild().getId()).get(panelSessionPageNumber.get(member.getId(), event.getGuild().getId()));
                    client.sendCommand(server, event.getOption("command").getAsString()).executeAsync();
                    event.getHook().editOriginal(Utils.getConfigMessage(event.getGuild().getId(), Messages.COMMAND_SENT)).queue();
                    break;
                case "rename":
                    ClientServer server1 = (ClientServer) memberServers.get(member.getId(), event.getGuild().getId()).get(panelSessionPageNumber.get(member.getId(), event.getGuild().getId()));
                    server1.getManager().setName(event.getOption("name").getAsString()).executeAsync();
                    event.getHook().editOriginal(Utils.getConfigMessage(event.getGuild().getId(), Messages.RENAMED_SERVER)).queue();
                    break;
                case "reinstall":
                    event.getHook().editOriginal(Utils.getConfigMessage(event.getGuild().getId(), Messages.REINSTALL_VERIFY)).setActionRow(Button.danger(member.getId() + member.getGuild().getId() + "m", "Yes")).queue();
                    break;

            }

        } else {
            event.getHook().editOriginal(DiscordBot.getGuildConfiguration.get("DMS", Messages.SEND_IN_SERVER)).queue();
        }

    }
}