package me.alexirving.bot;

import com.mattmalec.pterodactyl4j.DataType;
import com.mattmalec.pterodactyl4j.PteroBuilder;
import com.mattmalec.pterodactyl4j.client.entities.ClientServer;
import com.mattmalec.pterodactyl4j.client.entities.PteroClient;
import me.alexirving.bot.utils.ButtonSet;
import me.alexirving.bot.utils.Messages;
import me.alexirving.bot.utils.Mode;
import me.alexirving.bot.utils.Utils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.components.Button;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ServerManager {

    public static MessageEmbed updatePanel(Member member, Mode mode, PteroClient client) {
        String guildId = member.getGuild().getId();
        String memberId = member.getId();
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(member.getUser().getName() + "'s server manager!");
        embedBuilder.setThumbnail(Utils.getConfigMessage(guildId, Messages.PANEL_AVATAR_URL));
        switch (mode) {
            case PREVIOUS:
                if (Database.panelSessionPageNumber.get(guildId, memberId) - 1 < 0) {
                    Database.panelSessionPageNumber.put(guildId, memberId, Database.memberServers.get(guildId, memberId).size() - 1);
                } else {
                    Database.panelSessionPageNumber.put(guildId, memberId, Database.panelSessionPageNumber.get(guildId, memberId) - 1);
                }
                break;
            case NEXT:
                if (Database.panelSessionPageNumber.get(guildId, memberId) + 1 == Database.memberServers.get(guildId, memberId).size()) {
                    Database.panelSessionPageNumber.put(guildId, memberId, 0);
                } else {
                    Database.panelSessionPageNumber.put(guildId, memberId, Database.panelSessionPageNumber.get(guildId, memberId) + 1);
                }
                break;
            case UPDATE:
                break;
        }

        int count = 0;

        for (Object key : client.retrieveServers().execute().toArray()) {
            ClientServer server = (ClientServer) key;
            if (Database.panelSessionPageNumber.get(guildId, memberId).equals(count)) {
                embedBuilder.appendDescription(Utils.isOwner(server) + "** | " + server.getName() + " " + Utils.getStatus(Utils.getState(client, Database.memberServers.get(guildId, memberId).get(count))) + "**\n \r");
            } else {
                embedBuilder.appendDescription(Utils.isOwner(server) + " | " + server.getName() + " " + Utils.getStatus(Utils.getState(client, Database.memberServers.get(guildId, memberId).get(count))) + "\n \r");
            }
            count++;

        }
        //Return the embed
        return embedBuilder.build();


    }

    public static ClientServer getCurrentServer(Member member) {
        return Database.memberServers.get(member.getGuild().getId(), member.getId()).get(Database.panelSessionPageNumber.get(member.getGuild().getId(), member.getId()));
    }

    public static MessageEmbed generateInfoPanel(Member member) {
        String guildId = member.getGuild().getId();
        String memberId = member.getId();
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("Selected server: " + getCurrentServer(member).getName());
        PteroClient client;
        if (Utils.isLinkValid(Utils.getConfigMessage(member.getGuild().getId(), Messages.API_URL))){
            if (Objects.isNull(Database.memberPteroClients.get(guildId, memberId))) {

                Database.memberPteroClients.put(guildId, memberId, PteroBuilder.createClient(Utils.getConfigMessage(member.getGuild().getId(), Messages.API_URL), Database.currentToken.get(guildId, memberId)));
                client = Database.memberPteroClients.get(guildId, memberId);
            } else {
                client = Database.memberPteroClients.get(guildId, memberId);
            }
            builder.setThumbnail(Utils.getConfigMessage(guildId, Messages.INFO_AVATAR_URL));
            builder.appendDescription("**RAM: **" + Utils.getState(client, getCurrentServer(member)).getMemoryFormatted(DataType.MB).replace(" MB", "") + "/" + getCurrentServer(member).getLimits().getMemory() + "MB\n\r");
            builder.appendDescription("**CPU: **" + Math.floor(Utils.getState(client, getCurrentServer(member)).getCPU()) + "/" + getCurrentServer(member).getLimits().getCPU() + "%\n\r");
            builder.appendDescription("**DISK: **" + Utils.getState(client, getCurrentServer(member)).getDiskFormatted(DataType.GB).replace(" GB", "") + "/" + Integer.parseInt(getCurrentServer(member).getLimits().getDisk()) / 1000 + "GB\n\r");
            builder.appendDescription("**ID: **" + getCurrentServer(member).getIdentifier());



            return builder.build();
        }else{
            builder.setTitle("API_URL NOT SET CORRECTLY!");
            return builder.setColor(Color.RED).build();
        }
    }


    public static List<Button> generateButtons(Member member, ButtonSet buttonSet) {
        List<Button> list = new ArrayList<>();
        String memberId = member.getId();
        String guildId = member.getGuild().getId();


        switch (buttonSet) {
            case NAVIGATE -> {
                list.add(Button.primary(memberId + "b", "<"));
                list.add(Button.primary(memberId + "n", ">"));
                ClientServer server = Database.memberServers.get(guildId, memberId).get(Database.panelSessionPageNumber.get(guildId, memberId));
                list.add(Button.link(Utils.getConfigMessage(guildId, Messages.API_URL) + "server/" + server.getIdentifier(), "Go to panel"));
                list.add(Button.success(memberId + "f", "REFRESH"));
            }
            case ACTION -> {
                list.add(Button.primary(memberId + "s", "START"));
                list.add(Button.secondary(memberId + "r", "RESTART"));
                list.add(Button.danger(memberId + "p", "STOP"));
            }
            case KILL -> {
                list.add(Button.primary(memberId + "s", "START"));
                list.add(Button.secondary(memberId + "r", "RESTART"));
                list.add(Button.danger(memberId + "k", "KILL"));
            }
        }
        return list;
    }



}