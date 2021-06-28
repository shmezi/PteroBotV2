package me.alexirving.bot;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.gson.Gson;
import com.mattmalec.pterodactyl4j.client.entities.ClientServer;
import com.mattmalec.pterodactyl4j.client.entities.PteroClient;
import me.alexirving.bot.utils.Messages;
import net.dv8tion.jda.api.entities.Message;
import org.apache.commons.collections4.map.MultiKeyMap;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;

public class Database {

    public static Table<String, String, String> memberTokens = HashBasedTable.create();
    public static MultiKeyMap<String, String> currentToken = new MultiKeyMap<>();
    public static MultiKeyMap<String, PteroClient> memberPteroClients = new MultiKeyMap<>();
    public static MultiKeyMap<String, Integer> panelSessionPageNumber = new MultiKeyMap<>();
    public static MultiKeyMap<String, List<ClientServer>> memberServers = new MultiKeyMap<>();
    public static MultiKeyMap<String, Message> infoPanelSessions = new MultiKeyMap<>();
    public static MultiKeyMap<String, Message> panelSessions = new MultiKeyMap<>();
    public static Table<String, Messages, String> getGuildConfiguration = HashBasedTable.create();
    public static MultiKeyMap<String, Message> infoPanelSessionToDelete = new MultiKeyMap<>();
    public static MultiKeyMap<String, Message> panelSessionToDelete = new MultiKeyMap<>();

    public static void copyFile(File internalName, File externalName) throws IOException {
        // Grabs the file from within the jar to be copied outside of the jar
        InputStream fileSrc = Thread.currentThread().getContextClassLoader().getResourceAsStream(internalName.getPath());

        // Checks if the file exists so it doesn't overwrite existing settings each time it is run
        if (externalName.createNewFile()) {
            assert fileSrc != null;
            Files.copy(fileSrc, externalName.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
    }

    public static void updateTokenMaps() throws IOException {
        Gson gson = new Gson();
        Reader reader = Files.newBufferedReader(Paths.get("tokens.json"));
        Map<String, Map<String, String>> map = gson.fromJson(reader, Map.class);
        for (String loopValue : map.keySet()) {
            Map<String, String> map2 = map.get(loopValue);
            int u = 0;
            for (String key2 : map2.keySet()) {
                Database.memberTokens.put(loopValue, key2, (String) map2.values().toArray()[u]);
                u++;
            }


        }


    }

    public static void updateMessaegsMap() throws IOException {
        Gson gson = new Gson();
        Reader reader = Files.newBufferedReader(Paths.get("messages.json"));
        Map<String, Map<String, String>> map = gson.fromJson(reader, Map.class);
        for (String loopValue : map.keySet()) {
            Map<String, String> map2 = map.get(loopValue);
            int u = 0;
            for (String key2 : map2.keySet()) {
                Database.getGuildConfiguration.put(loopValue, Messages.valueOf(key2), (String) map2.values().toArray()[u]);
                u++;
            }


        }


    }

    public static void updateTokensJson() throws IOException {
        Writer writer = new FileWriter("tokens.json");
        Gson gson = new Gson();
        String object = gson.toJson(Database.memberTokens.rowMap(), Map.class);
        writer.write(object);
        writer.close();

    }

    public static void updateMessagesJson() throws IOException {
        Writer writer = new FileWriter("messages.json");
        Gson gson = new Gson();
        String object = gson.toJson(Database.getGuildConfiguration.rowMap(), Map.class);
        writer.write(object);
        writer.close();

    }
}
