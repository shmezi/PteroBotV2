package me.alexirivng.bot.events;

import com.mattmalec.pterodactyl4j.PowerAction;
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
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.List;

import static me.alexirivng.bot.DiscordBot.memberServers;
import static me.alexirivng.bot.DiscordBot.panelSessionPageNumber;


public class ButtonClick extends ListenerAdapter {
    @Override
    public void onButtonClick(ButtonClickEvent event) {
        event.deferEdit().queue();
        Member member = event.getGuild().getMemberById(new StringBuffer(event.getComponentId()).deleteCharAt(event.getComponentId().length() - 1).toString().replace(event.getGuild().getId(), ""));
        String command = event.getComponentId().replace(event.getMember().getId(), "").replace(event.getGuild().getId(), "");
        PteroClient pteroClient = PteroBuilder.createClient(Utils.getConfigMessage(member.getGuild().getId(), Messages.API_URL), DiscordBot.currentToken.get(member.getId(), event.getGuild().getId()));
        switch (command) {
            case "n":
                event.getHook().editOriginalEmbeds((ServerManager.updatePanel(member, Mode.NEXT, pteroClient))).setActionRow(ServerManager.generateButtons(member, ButtonSet.NAVIGATE)).queue();
                DiscordBot.infoPanelSessions.get(event.getMember().getId(), event.getGuild().getId()).editMessageEmbeds(ServerManager.generateInfoPanel(member)).setActionRow(ServerManager.generateButtons(member, ButtonSet.ACTION)).queue();
                break;
            case "b":
                event.getHook().editOriginalEmbeds((ServerManager.updatePanel(member, Mode.PREVIOUS, pteroClient))).setActionRow(ServerManager.generateButtons(member, ButtonSet.NAVIGATE)).queue();
                DiscordBot.infoPanelSessions.get(event.getMember().getId(), event.getGuild().getId()).editMessageEmbeds(ServerManager.generateInfoPanel(member)).setActionRow(ServerManager.generateButtons(member, ButtonSet.ACTION)).queue();
                break;
            case "s":
                ClientServer server = (ClientServer) memberServers.get(member.getId(), event.getGuild().getId()).get(panelSessionPageNumber.get(member.getId(), event.getGuild().getId()));
                pteroClient.setPower(server, PowerAction.START).executeAsync();
                event.getHook().editOriginalEmbeds(ServerManager.generateInfoPanel(member)).setActionRow(ServerManager.generateButtons(member, ButtonSet.ACTION)).queue();
                DiscordBot.panelSessions.get(member.getId(), event.getGuild().getId()).editMessageEmbeds(ServerManager.updatePanel(member, Mode.UPDATE, pteroClient)).setActionRow(ServerManager.generateButtons(member, ButtonSet.NAVIGATE)).queue();
                break;
            case "r":
                ClientServer server1 = (ClientServer) memberServers.get(member.getId(), event.getGuild().getId()).get(panelSessionPageNumber.get(member.getId(), event.getGuild().getId()));
                pteroClient.setPower(server1, PowerAction.RESTART).executeAsync();
                event.getHook().editOriginalEmbeds(ServerManager.generateInfoPanel(member)).setActionRow(ServerManager.generateButtons(member, ButtonSet.ACTION)).queue();
                DiscordBot.panelSessions.get(member.getId(), event.getGuild().getId()).editMessageEmbeds(ServerManager.updatePanel(member, Mode.UPDATE, pteroClient)).setActionRow(ServerManager.generateButtons(member, ButtonSet.NAVIGATE)).queue();
                break;
            case "p":
                ClientServer server2 = (ClientServer) memberServers.get(member.getId(), event.getGuild().getId()).get(panelSessionPageNumber.get(member.getId(), event.getGuild().getId()));
                pteroClient.setPower(server2, PowerAction.STOP).executeAsync();
                event.getHook().editOriginalEmbeds(ServerManager.generateInfoPanel(member)).setActionRow(ServerManager.generateButtons(member, ButtonSet.KILL)).queue();
                DiscordBot.panelSessions.get(member.getId(), event.getGuild().getId()).editMessageEmbeds(ServerManager.updatePanel(member, Mode.UPDATE, pteroClient)).setActionRow(ServerManager.generateButtons(member, ButtonSet.NAVIGATE)).queue();
                break;
            case "k":
                ClientServer server3 = (ClientServer) memberServers.get(member.getId(), event.getGuild().getId()).get(panelSessionPageNumber.get(member.getId(), event.getGuild().getId()));
                pteroClient.setPower(server3, PowerAction.KILL).executeAsync();
                event.getHook().editOriginalEmbeds(ServerManager.generateInfoPanel(member)).setActionRow(ServerManager.generateButtons(member, ButtonSet.ACTION)).queue();
                DiscordBot.panelSessions.get(member.getId(), event.getGuild().getId()).editMessageEmbeds(ServerManager.updatePanel(member, Mode.UPDATE, pteroClient)).setActionRow(ServerManager.generateButtons(member, ButtonSet.NAVIGATE)).queue();
                break;
            case "f":
                event.getHook().editOriginalEmbeds((ServerManager.updatePanel(member, Mode.UPDATE, pteroClient))).setActionRow(ServerManager.generateButtons(member, ButtonSet.NAVIGATE)).queue();
                DiscordBot.infoPanelSessions.get(event.getMember().getId(), event.getGuild().getId()).editMessageEmbeds(ServerManager.generateInfoPanel(member)).setActionRow(ServerManager.generateButtons(member, ButtonSet.ACTION)).queue();
                break;
            case "m":
                ClientServer server4 = (ClientServer) memberServers.get(member.getId(), event.getGuild().getId()).get(panelSessionPageNumber.get(member.getId(), event.getGuild().getId()));
                server4.getManager().reinstall().executeAsync();
                event.getHook().editOriginal(Utils.getConfigMessage(event.getGuild().getId(), Messages.REINSTALL)).queue();

                break;
        }

    }
}
