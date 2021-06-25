package me.alexirivng.bot;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.mattmalec.pterodactyl4j.client.entities.PteroClient;
import me.alexirivng.bot.events.ButtonClick;
import me.alexirivng.bot.events.SendMessage;
import me.alexirivng.bot.events.ShutDown;
import me.alexirivng.bot.events.SlashCommand;
import me.alexirivng.bot.utils.Messages;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Message;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static me.alexirivng.bot.Utils.updateActivity;

public class DiscordBot {
    public static Table<String, String, String> memberTokens = HashBasedTable.create();
    public static Table<String, String, String> currentToken = HashBasedTable.create();
    public static Table<String, String, PteroClient> memberPteroClients = HashBasedTable.create();
    public static Table<String, String, Integer> panelSessionPageNumber = HashBasedTable.create();
    public static Table<String, String, List> memberServers = HashBasedTable.create();
    public static Table<String, String, Message> infoPanelSessions = HashBasedTable.create();
    public static Table<String, String, Message> panelSessions = HashBasedTable.create();
    public static Table<String, Messages, String> getGuildConfiguration = HashBasedTable.create();
    public static Table<String, String, Message> infoPanelSessionToDelete = HashBasedTable.create();
    public static Table<String, String, Message> panelSessionToDelete = HashBasedTable.create();
    public static void main(String[] arguments) throws Exception {

        JDA api = JDABuilder.createDefault(Config.TOKEN()).build();
        api.addEventListener(new SlashCommand());
        api.addEventListener(new ButtonClick());
        api.addEventListener(new SendMessage());
        api.addEventListener(new ShutDown());
        DiscordBot.getGuildConfiguration.put("DEFAULT", Messages.NOT_LINKED, "You aren't linked pall!");
        DiscordBot.getGuildConfiguration.put("DEFAULT", Messages.INVALID_APIKEY, "Invalid API  KEYl!");
        DiscordBot.getGuildConfiguration.put("DEFAULT", Messages.LINKED, "You linked pall!");
        DiscordBot.getGuildConfiguration.put("DEFAULT", Messages.NOT_YOUR_ACCOUNT, "This aint ur account!");
        DiscordBot.getGuildConfiguration.put("DEFAULT", Messages.SEND_IN_SERVER, "Send in ur server, not dis dms!");
        DiscordBot.getGuildConfiguration.put("DEFAULT", Messages.PANEL_CREATED, "Panel created!");
        DiscordBot.getGuildConfiguration.put("DEFAULT", Messages.STAFF_ID, "857356342120677418");
        DiscordBot.getGuildConfiguration.put("DEFAULT", Messages.API_URL, "https://devpanel.mysticnode.net/");
        DiscordBot.getGuildConfiguration.put("DEFAULT", Messages.PANEL_AVATAR_URL, "https://cdn.discordapp.com/attachments/830727232094732309/857347507423543346/854098976180404264.png");
        DiscordBot.getGuildConfiguration.put("DEFAULT", Messages.INFO_AVATAR_URL, "https://cdn.discordapp.com/attachments/830727232094732309/857732267375984690/2998605-200.png");
        DiscordBot.getGuildConfiguration.put("DEFAULT", Messages.NO_SERVER, "No servers on account!");
        DiscordBot.getGuildConfiguration.put("DEFAULT",Messages.COMMAND_SENT,"Command has been sent!");
        DiscordBot.getGuildConfiguration.put("DEFAULT",Messages.RENAMED_SERVER,"Renamed server!");
        DiscordBot.getGuildConfiguration.put("DEFAULT",Messages.REINSTALL,"Server is now reinstalling!");
        DiscordBot.getGuildConfiguration.put("DEFAULT",Messages.REINSTALL_VERIFY,"Are you sure you want to reinstall this server?");
        DiscordBot.getGuildConfiguration.put("DMS",Messages.SEND_IN_SERVER,"Please send dis' in a server :)");
        updateActivity(api);
//
//        try {
//            ServerManager.updateHashmaps();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


//        api.retrieveCommands().queue(commands -> {
//                    CommandData link = new CommandData("link", "Command to link account to pterodactyl.").addOption(OptionType.STRING, "key", "API key from panel", true);
//                    CommandData servers = new CommandData("servers", "Command to get your servers.").addOption(OptionType.USER, "user", "Check a server of another user", false);
//                    CommandData server = new CommandData("server", "Command to get your servers.").addOption(OptionType.USER, "user", "Check a server of another user", false);
//                    CommandData s = new CommandData("s", "Command to get your servers.").addOption(OptionType.USER, "user", "Check a server of another user", false);
//                    CommandData send = new CommandData("send", "Send a command to the current selected server!").addOption(OptionType.STRING, "command", "Command to send to server", true);
//                    CommandData reinstall = new CommandData("reinstall", "Reinstalls the current selected server");
//                    CommandData rename = new CommandData("rename", "Renames the current server").addOption(OptionType.STRING, "name", "The name to set the current selected server to.", true);
//                    api.updateCommands().addCommands(link, servers, server,s,send,reinstall,rename).queue();
//                    api.retrieveCommandById("info").complete().delete().queue();
//                }
//        );

    }
}


