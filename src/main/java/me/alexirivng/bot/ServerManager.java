package me.alexirivng.bot;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mattmalec.pterodactyl4j.DataType;
import com.mattmalec.pterodactyl4j.PteroBuilder;
import com.mattmalec.pterodactyl4j.client.entities.ClientServer;
import com.mattmalec.pterodactyl4j.client.entities.PteroClient;
import me.alexirivng.bot.utils.ButtonSet;
import me.alexirivng.bot.utils.Messages;
import me.alexirivng.bot.utils.Mode;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.components.Button;
import org.apache.commons.collections4.map.MultiKeyMap;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static me.alexirivng.bot.Utils.*;

public class ServerManager {

    public static MessageEmbed updatePanel(Member member, Mode mode, PteroClient client) {
        switch (mode) {
            case PREVIOUS:
                if (DiscordBot.panelSessionPageNumber.get(member.getId(), member.getGuild().getId()) - 1 < 0) {
                    DiscordBot.panelSessionPageNumber.put(member.getId(), member.getGuild().getId(), DiscordBot.memberServers.get(member.getId(), member.getGuild().getId()).size() - 1);
                } else {
                    DiscordBot.panelSessionPageNumber.put(member.getId(), member.getGuild().getId(), DiscordBot.panelSessionPageNumber.get(member.getId(), member.getGuild().getId()) - 1);
                }
                ;
                break;
            case NEXT:
                if (DiscordBot.panelSessionPageNumber.get(member.getId(), member.getGuild().getId()) + 1 == DiscordBot.memberServers.get(member.getId(), member.getGuild().getId()).size()) {
                    DiscordBot.panelSessionPageNumber.put(member.getId(), member.getGuild().getId(), 0);
                } else {
                    DiscordBot.panelSessionPageNumber.put(member.getId(), member.getGuild().getId(), DiscordBot.panelSessionPageNumber.get(member.getId(), member.getGuild().getId()) + 1);
                }
                break;
            case UPDATE:
                break;
        }
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(member.getUser().getName() + "'s server manager!");
        embedBuilder.setThumbnail(Utils.getConfigMessage(member.getGuild().getId(), Messages.PANEL_AVATAR_URL));
        int count = 0;
        if (client.retrieveServers().execute().size() != 0) {
            for (Object key : client.retrieveServers().execute().toArray()) {
                ClientServer server = (ClientServer) key;
                if (DiscordBot.panelSessionPageNumber.get(member.getId(), member.getGuild().getId()).equals(count)) {
                    embedBuilder.appendDescription(getIsOWner(server) + "** | " + server.getName() + " " + getStatus(getState(client, (ClientServer) DiscordBot.memberServers.get(member.getId(), member.getGuild().getId()).get(count))) + "**\n \r");
                    count++;
                } else {
                    embedBuilder.appendDescription(getIsOWner(server) + " | " + server.getName() + " " + getStatus(getState(client, (ClientServer) DiscordBot.memberServers.get(member.getId(), member.getGuild().getId()).get(count))) + "\n \r");
                    count++;
                }
            }
        } else {
            embedBuilder.appendDescription(Utils.getConfigMessage(member.getGuild().getId(), Messages.NO_SERVER));
        }
        //Return the embed
        return embedBuilder.build();


    }

    public static ClientServer getCurrenServer(Member member) {
        return (ClientServer) DiscordBot.memberServers.get(member.getId(), member.getGuild().getId()).get(DiscordBot.panelSessionPageNumber.get(member.getId(), member.getGuild().getId()));
    }

    public static MessageEmbed generateInfoPanel(Member member) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("Selected server: " + getCurrenServer(member).getName());
        PteroClient client = PteroBuilder.createClient(Utils.getConfigMessage(member.getGuild().getId(), Messages.API_URL), DiscordBot.currentToken.get(member.getId(), member.getGuild().getId()));
        builder.setThumbnail(Utils.getConfigMessage(member.getGuild().getId(), Messages.INFO_AVATAR_URL));
        builder.appendDescription("**RAM: **" + getState(client, getCurrenServer(member)).getMemoryFormatted(DataType.MB).replace(" MB", "") + "/" + getCurrenServer(member).getLimits().getMemory() + "MB\n\r");
        builder.appendDescription("**CPU: **" + Math.floor(getState(client, getCurrenServer(member)).getCPU()) + "/" + getCurrenServer(member).getLimits().getCPU() + "%\n\r");
        builder.appendDescription("**DISK: **" + getState(client, getCurrenServer(member)).getDiskFormatted(DataType.GB).replace(" GB", "") + "/" + Integer.parseInt(getCurrenServer(member).getLimits().getDisk()) / 1000 + "GB\n\r");
        builder.appendDescription("**ID: **" + getCurrenServer(member).getIdentifier());

        return builder.build();
    }


    public static List<Button> generateButtons(Member member, ButtonSet buttonSet) {
        List<Button> list = new ArrayList<Button>();
        switch (buttonSet) {
            case NAVIGATE:
                list.add(Button.primary(member.getId() + member.getGuild().getId() + "b", "<"));
                list.add(Button.primary(member.getId() + member.getGuild().getId() + "n", ">"));
                ClientServer server = (ClientServer) DiscordBot.memberServers.get(member.getId(), member.getGuild().getId()).get(DiscordBot.panelSessionPageNumber.get(member.getId(), member.getGuild().getId()));
                list.add(Button.link(Utils.getConfigMessage(member.getGuild().getId(), Messages.API_URL) + "server/" + server.getIdentifier(), "Go to panel"));
                list.add(Button.success(member.getId() + member.getGuild().getId() + "f", "REFRESH"));
                break;
            case ACTION:
                list.add(Button.primary(member.getId() + member.getGuild().getId() + "s", "START"));
                list.add(Button.secondary(member.getId() + member.getGuild().getId() + "r", "RESTART"));
                list.add(Button.danger(member.getId() + member.getGuild().getId() + "p", "STOP"));
                break;
            case KILL:
                list.add(Button.primary(member.getId() + member.getGuild().getId() + "s", "START"));
                list.add(Button.secondary(member.getId() + member.getGuild().getId() + "r", "RESTART"));
                list.add(Button.danger(member.getId() + member.getGuild().getId() + "k", "KILL"));
                break;

        }
        return list;
    }


//    public static void updateHashmaps() throws IOException {
//        Gson gson = new GsonBuilder()
//                .setPrettyPrinting()
//                .registerTypeHierarchyAdapter(Table.class, new TableTypeHierarchyAdapter())
//                .create();
//        try (FileReader reader = new FileReader("table.json")) {
//            Type typeOfTable = new TypeToken<Table<String, String, String>>() {}.getType();
//            Table<String, String, String> readTable = gson.fromJson(reader, typeOfTable);
//            assert table.equals(readTable);
//        }
//    }

    public static void updateJsons() throws IOException {
        Writer writer = new FileWriter("user.json");
        new Gson().toJson(DiscordBot.memberTokens, writer);
        writer.close();


    }
}