package me.alexirving.bot;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import me.alexirving.bot.events.ButtonClick;
import me.alexirving.bot.events.GuildJoin;
import me.alexirving.bot.events.MessageSend;
import me.alexirving.bot.events.SlashCommand;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;

import static me.alexirving.bot.utils.Utils.updateActivity;

public class DiscordBot {

    public static void main(String[] arguments) throws Exception {
        Database.copyFile(new File("messages.json"),new File("messages.json"));
        Database.copyFile(new File("tokens.json"),new File("tokens.json"));
        Database.copyFile(new File("token.json"),new File("token.json"));
        Reader reader = Files.newBufferedReader(Paths.get("token.json"));
        JsonObject jobj = new Gson().fromJson(reader, JsonObject.class);
        String token = jobj.get("TOKEN").getAsString();
        JDA api = JDABuilder.createDefault(token).build();
        api.addEventListener(new SlashCommand());
        api.addEventListener(new MessageSend());
        api.addEventListener(new ButtonClick());
        api.addEventListener(new GuildJoin());
        try {
            Database.updateMessaegsMap();
            Database.updateTokenMaps();
            updateActivity(api);
        } catch (IOException e) {
            e.printStackTrace();

        }

    }

}


