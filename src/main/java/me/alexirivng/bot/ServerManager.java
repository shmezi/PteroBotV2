package me.alexirivng.bot;

import com.google.gson.Gson;
import com.mattmalec.pterodactyl4j.DataType;
import com.mattmalec.pterodactyl4j.PteroBuilder;
import com.mattmalec.pterodactyl4j.client.entities.ClientServer;
import com.mattmalec.pterodactyl4j.client.entities.PteroClient;
import me.alexirivng.bot.utils.ButtonSet;
import me.alexirivng.bot.utils.Messages;
import me.alexirivng.bot.utils.Mode;
import me.alexirivng.bot.utils.Utils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.components.Button;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static me.alexirivng.bot.DiscordBot.memberPteroClients;
import static me.alexirivng.bot.utils.Utils.*;

public class ServerManager {

    public static MessageEmbed updatePanel(Member member, Mode mode, PteroClient client) {
        String guildId = member.getGuild().getId();
        String memberId = member.getId();
        switch (mode) {
            case PREVIOUS:
                if (DiscordBot.panelSessionPageNumber.get(guildId, memberId) - 1 < 0) {
                    DiscordBot.panelSessionPageNumber.put(guildId, memberId, DiscordBot.memberServers.get(guildId, memberId).size() - 1);
                } else {
                    DiscordBot.panelSessionPageNumber.put(guildId, memberId, DiscordBot.panelSessionPageNumber.get(guildId, memberId) - 1);
                }
                break;
            case NEXT:
                if (DiscordBot.panelSessionPageNumber.get(guildId, memberId) + 1 == DiscordBot.memberServers.get(guildId, memberId).size()) {
                    DiscordBot.panelSessionPageNumber.put(guildId, memberId, 0);
                } else {
                    DiscordBot.panelSessionPageNumber.put(guildId, memberId, DiscordBot.panelSessionPageNumber.get(guildId, memberId) + 1);
                }
                break;
            case UPDATE:
                break;
        }
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(member.getUser().getName() + "'s server manager!");
        embedBuilder.setThumbnail(Utils.getConfigMessage(guildId, Messages.PANEL_AVATAR_URL));
        int count = 0;

        for (Object key : client.retrieveServers().execute().toArray()) {
            ClientServer server = (ClientServer) key;
            if (DiscordBot.panelSessionPageNumber.get(guildId, memberId).equals(count)) {
                embedBuilder.appendDescription(getIsOwner(server) + "** | " + server.getName() + " " + getStatus(getState(client, DiscordBot.memberServers.get(guildId, memberId).get(count))) + "**\n \r");
            } else {
                embedBuilder.appendDescription(getIsOwner(server) + " | " + server.getName() + " " + getStatus(getState(client, DiscordBot.memberServers.get(guildId, memberId).get(count))) + "\n \r");
            }
            count++;

        }
        //Return the embed
        return embedBuilder.build();


    }

    public static ClientServer getCurrenServer(Member member) {
        return DiscordBot.memberServers.get(member.getGuild().getId(), member.getId()).get(DiscordBot.panelSessionPageNumber.get(member.getGuild().getId(), member.getId()));
    }

    public static MessageEmbed generateInfoPanel(Member member) {
        String guildId = member.getGuild().getId();
        String memberId = member.getId();
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("Selected server: " + getCurrenServer(member).getName());
        PteroClient client;
        if (Objects.isNull(memberPteroClients.get(guildId, memberId))) {
            memberPteroClients.put(guildId, memberId, PteroBuilder.createClient(Utils.getConfigMessage(member.getGuild().getId(), Messages.API_URL), DiscordBot.currentToken.get(guildId, memberId)));
            client = memberPteroClients.get(guildId, memberId);
        } else {
            client = memberPteroClients.get(guildId, memberId);
        }
        builder.setThumbnail(Utils.getConfigMessage(guildId, Messages.INFO_AVATAR_URL));
        builder.appendDescription("**RAM: **" + getState(client, getCurrenServer(member)).getMemoryFormatted(DataType.MB).replace(" MB", "") + "/" + getCurrenServer(member).getLimits().getMemory() + "MB\n\r");
        builder.appendDescription("**CPU: **" + Math.floor(getState(client, getCurrenServer(member)).getCPU()) + "/" + getCurrenServer(member).getLimits().getCPU() + "%\n\r");
        builder.appendDescription("**DISK: **" + getState(client, getCurrenServer(member)).getDiskFormatted(DataType.GB).replace(" GB", "") + "/" + Integer.parseInt(getCurrenServer(member).getLimits().getDisk()) / 1000 + "GB\n\r");
        builder.appendDescription("**ID: **" + getCurrenServer(member).getIdentifier());

        return builder.build();
    }


    public static List<Button> generateButtons(Member member, ButtonSet buttonSet) {
        List<Button> list = new ArrayList<>();
        String memberId = member.getId();
        String guildId = member.getGuild().getId();


        switch (buttonSet) {
            case NAVIGATE -> {
                list.add(Button.primary(memberId + "b", "<"));
                list.add(Button.primary(memberId + "n", ">"));
                ClientServer server = DiscordBot.memberServers.get(guildId, memberId).get(DiscordBot.panelSessionPageNumber.get(guildId, memberId));
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


    public static void updateHashmaps() throws IOException {
        Gson gson = new Gson();
        Reader reader = Files.newBufferedReader(Paths.get("user.json"));
        Map<String, Map<String, String>> map = gson.fromJson(reader, Map.class);
        for (String loopValue : map.keySet()) {
            Map<String, String> map2 = map.get(loopValue);
            int u = 0;
            for (String key2 : map2.keySet()) {
                DiscordBot.memberTokens.put(loopValue, key2, (String) map2.values().toArray()[u]);
                u++;
            }


        }


    }


    public static void updateJsons() throws IOException {
        Writer writer = new FileWriter("user.json");
        Gson gson = new Gson();
        String object = gson.toJson(DiscordBot.memberTokens.rowMap(), Map.class);
        writer.write(object);
        writer.close();


    }
}