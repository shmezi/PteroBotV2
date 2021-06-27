package me.alexirivng.bot.events;

import com.mattmalec.pterodactyl4j.PowerAction;
import com.mattmalec.pterodactyl4j.PteroBuilder;
import com.mattmalec.pterodactyl4j.client.entities.ClientServer;
import com.mattmalec.pterodactyl4j.client.entities.PteroClient;
import me.alexirivng.bot.DiscordBot;
import me.alexirivng.bot.ServerManager;
import me.alexirivng.bot.utils.ButtonSet;
import me.alexirivng.bot.utils.Messages;
import me.alexirivng.bot.utils.Mode;
import me.alexirivng.bot.utils.Utils;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Objects;

import static me.alexirivng.bot.DiscordBot.*;


public class ButtonClick extends ListenerAdapter {
    @Override
    public void onButtonClick(ButtonClickEvent event) {
        if (!event.isAcknowledged()) {
            event.deferEdit().queue();
        }
        String guildId = Objects.requireNonNull(event.getGuild()).getId();
        Member member = event.getGuild().retrieveMemberById(new StringBuffer(event.getComponentId()).deleteCharAt(event.getComponentId().length() - 1).toString()).complete();
        String memberId;
        if (!Objects.isNull(member)) {
            memberId = member.getId();
            if (!Objects.isNull(DiscordBot.currentToken.get(guildId, memberId))) {
                PteroClient pteroClient;
                if (Objects.isNull(memberPteroClients.get(guildId, memberId))) {
                    memberPteroClients.put(guildId,memberId,PteroBuilder.createClient(Utils.getConfigMessage(member.getGuild().getId(), Messages.API_URL), DiscordBot.currentToken.get(guildId, memberId)));
                    pteroClient = memberPteroClients.get(guildId, memberId);
                } else {
                    pteroClient = memberPteroClients.get(guildId, memberId);
                }
                String command = event.getComponentId().replace(memberId, "").replace(guildId, "");
                switch (command) {
                    case "n" -> {
                        event.getHook().editOriginalEmbeds((ServerManager.updatePanel(member, Mode.NEXT, pteroClient))).setActionRow(ServerManager.generateButtons(member, ButtonSet.NAVIGATE)).queue();
                        infoPanelSessions.get(guildId, memberId).editMessageEmbeds(ServerManager.generateInfoPanel(member)).setActionRow(ServerManager.generateButtons(member, ButtonSet.ACTION)).queue();
                    }
                    case "b" -> {
                        event.getHook().editOriginalEmbeds((ServerManager.updatePanel(member, Mode.PREVIOUS, pteroClient))).setActionRow(ServerManager.generateButtons(member, ButtonSet.NAVIGATE)).queue();
                        infoPanelSessions.get(guildId, memberId).editMessageEmbeds(ServerManager.generateInfoPanel(member)).setActionRow(ServerManager.generateButtons(member, ButtonSet.ACTION)).queue();
                    }
                    case "s" -> {
                        ClientServer server = memberServers.get(guildId, memberId).get(panelSessionPageNumber.get(guildId, memberId));
                        pteroClient.setPower(server, PowerAction.START).executeAsync();
                        event.getHook().editOriginalEmbeds(ServerManager.generateInfoPanel(member)).setActionRow(ServerManager.generateButtons(member, ButtonSet.ACTION)).queue();
                        panelSessions.get(guildId, memberId).editMessageEmbeds(ServerManager.updatePanel(member, Mode.UPDATE, pteroClient)).setActionRow(ServerManager.generateButtons(member, ButtonSet.NAVIGATE)).queue();
                    }
                    case "r" -> {
                        ClientServer server = memberServers.get(guildId, memberId).get(panelSessionPageNumber.get(guildId, memberId));
                        pteroClient.setPower(server, PowerAction.RESTART).executeAsync();
                        event.getHook().editOriginalEmbeds(ServerManager.generateInfoPanel(member)).setActionRow(ServerManager.generateButtons(member, ButtonSet.ACTION)).queue();
                        DiscordBot.panelSessions.get(guildId, memberId).editMessageEmbeds(ServerManager.updatePanel(member, Mode.UPDATE, pteroClient)).setActionRow(ServerManager.generateButtons(member, ButtonSet.NAVIGATE)).queue();
                    }
                    case "p" -> {
                        ClientServer server = memberServers.get(guildId, memberId).get(panelSessionPageNumber.get(guildId, memberId));
                        pteroClient.setPower(server, PowerAction.STOP).executeAsync();
                        event.getHook().editOriginalEmbeds(ServerManager.generateInfoPanel(member)).setActionRow(ServerManager.generateButtons(member, ButtonSet.KILL)).queue();
                        DiscordBot.panelSessions.get(guildId, memberId).editMessageEmbeds(ServerManager.updatePanel(member, Mode.UPDATE, pteroClient)).setActionRow(ServerManager.generateButtons(member, ButtonSet.NAVIGATE)).queue();
                    }
                    case "k" -> {
                        ClientServer server = memberServers.get(guildId, memberId).get(panelSessionPageNumber.get(guildId, memberId));
                        pteroClient.setPower(server, PowerAction.KILL).executeAsync();
                        event.getHook().editOriginalEmbeds(ServerManager.generateInfoPanel(member)).setActionRow(ServerManager.generateButtons(member, ButtonSet.ACTION)).queue();
                        DiscordBot.panelSessions.get(guildId, memberId).editMessageEmbeds(ServerManager.updatePanel(member, Mode.UPDATE, pteroClient)).setActionRow(ServerManager.generateButtons(member, ButtonSet.NAVIGATE)).queue();
                    }
                    case "f" -> {
                        event.getHook().editOriginalEmbeds((ServerManager.updatePanel(member, Mode.UPDATE, pteroClient))).setActionRow(ServerManager.generateButtons(member, ButtonSet.NAVIGATE)).queue();
                        infoPanelSessions.get(guildId, memberId).editMessageEmbeds(ServerManager.generateInfoPanel(member)).setActionRow(ServerManager.generateButtons(member, ButtonSet.ACTION)).queue();
                    }
                    case "m" -> {
                        ClientServer server = memberServers.get(guildId, memberId).get(panelSessionPageNumber.get(guildId, memberId));
                        server.getManager().reinstall().executeAsync();
                        event.getHook().editOriginal(Utils.getConfigMessage(guildId, Messages.REINSTALL)).queue();
                    }
                }

            } else {
                Objects.requireNonNull(event.getMessage()).delete().queue();

            }
        } else {
            Objects.requireNonNull(event.getMessage()).delete().queue();
        }
    }
}