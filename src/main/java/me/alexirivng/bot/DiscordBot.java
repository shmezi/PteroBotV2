package me.alexirivng.bot;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.mattmalec.pterodactyl4j.client.entities.ClientServer;
import com.mattmalec.pterodactyl4j.client.entities.PteroClient;
import me.alexirivng.bot.events.ButtonClick;
import me.alexirivng.bot.events.SlashCommand;
import me.alexirivng.bot.utils.Messages;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Message;
import org.apache.commons.collections4.map.MultiKeyMap;

import java.io.IOException;
import java.util.List;

import static me.alexirivng.bot.utils.Utils.updateActivity;

public class DiscordBot {
    public static Table<String, String, String> memberTokens = HashBasedTable.create();
    public static MultiKeyMap<String, String> currentToken = new MultiKeyMap<>();
    public static MultiKeyMap<String, PteroClient> memberPteroClients = new MultiKeyMap<>();
    public static MultiKeyMap<String, Integer> panelSessionPageNumber = new MultiKeyMap<>();
    public static MultiKeyMap<String, List<ClientServer>> memberServers = new MultiKeyMap<>();
    public static MultiKeyMap<String, Message> infoPanelSessions = new MultiKeyMap<>();
    public static MultiKeyMap<String, Message> panelSessions = new MultiKeyMap<>();
    public static MultiKeyMap<String, String> getGuildConfiguration = new MultiKeyMap<>();
    public static MultiKeyMap<String, Message> infoPanelSessionToDelete = new MultiKeyMap<>();
    public static MultiKeyMap<String, Message> panelSessionToDelete = new MultiKeyMap<>();

    public static void main(String[] arguments) throws Exception {

        JDA api = JDABuilder.createDefault(Config.TOKEN()).build();
        api.addEventListener(new SlashCommand());
        api.addEventListener(new ButtonClick());
        DiscordBot.getGuildConfiguration.put("DEFAULT", Messages.NOT_LINKED.toString(), "You aren't linked pall!");
        DiscordBot.getGuildConfiguration.put("DEFAULT", Messages.INVALID_APIKEY.toString(), "Invalid API  KEYl!");
        DiscordBot.getGuildConfiguration.put("DEFAULT", Messages.LINKED.toString(), "You linked pall!");
        DiscordBot.getGuildConfiguration.put("DEFAULT", Messages.NOT_YOUR_ACCOUNT.toString(), "This aint ur account!");
        DiscordBot.getGuildConfiguration.put("DEFAULT", Messages.SEND_IN_SERVER.toString(), "Send in ur server, not dis dms!");
        DiscordBot.getGuildConfiguration.put("DEFAULT", Messages.PANEL_CREATED.toString(), "Panel created!");
        DiscordBot.getGuildConfiguration.put("DEFAULT", Messages.STAFF_ID.toString(), "857356342120677418");
        DiscordBot.getGuildConfiguration.put("DEFAULT", Messages.API_URL.toString(), "https://devpanel.mysticnode.net/");
        DiscordBot.getGuildConfiguration.put("DEFAULT", Messages.PANEL_AVATAR_URL.toString(), "https://cdn.discordapp.com/attachments/830727232094732309/857347507423543346/854098976180404264.png");
        DiscordBot.getGuildConfiguration.put("DEFAULT", Messages.INFO_AVATAR_URL.toString(), "https://cdn.discordapp.com/attachments/830727232094732309/857732267375984690/2998605-200.png");
        DiscordBot.getGuildConfiguration.put("DEFAULT", Messages.NO_SERVER.toString(), "No servers on account!");
        DiscordBot.getGuildConfiguration.put("DEFAULT", Messages.COMMAND_SENT.toString(), "Command has been sent!");
        DiscordBot.getGuildConfiguration.put("DEFAULT", Messages.RENAMED_SERVER.toString(), "Renamed server!");
        DiscordBot.getGuildConfiguration.put("DEFAULT", Messages.REINSTALL.toString(), "Server is now reinstalling!");
        DiscordBot.getGuildConfiguration.put("DEFAULT", Messages.REINSTALL_VERIFY.toString(), "Are you sure you want to reinstall this server?");
        DiscordBot.getGuildConfiguration.put("DMS", Messages.SEND_IN_SERVER.toString(), "Please send dis' in a server :)");


        try {
            ServerManager.updateHashmaps();
            updateActivity(api);
        } catch (IOException e) {
            e.printStackTrace();

        }


//        CommandData access = new CommandData("access", "Add your account as  a sub-user to another user").addOption(OptionType.STRING, "id", "Server ID to be added to", true);
//        CommandData create = new CommandData("create", "Create a server")
//                .addOption(OptionType.USER, "owner", "Owner for server", true)
//                .addOption(OptionType.STRING, "egg", "Egg for server", true)
//                .addOption(OptionType.INTEGER, "ram", "Ram limit for server in MB", true)
//                .addOption(OptionType.INTEGER, "cpu", "CPU limit for server in MB", true)
//                .addOption(OptionType.INTEGER, "diskspace", "Disk limit for server", true)
//                .addOption(OptionType.STRING, "name", "Name for server", true)
//                .addOption(OptionType.STRING, "description", "Name for server", false);
//        CommandData link = new CommandData("link", "Command to link account to pterodactyl.").addOption(OptionType.STRING, "key", "API key from panel", true);
//        CommandData servers = new CommandData("servers", "Command to get your servers.").addOption(OptionType.USER, "user", "Check a server of another user", false);
//        CommandData server = new CommandData("server", "Command to get your servers.").addOption(OptionType.USER, "user", "Check a server of another user", false);
//        CommandData s = new CommandData("s", "Command to get your servers.").addOption(OptionType.USER, "user", "Check a server of another user", false);
//        CommandData send = new CommandData("send", "Send a command to the current selected server!").addOption(OptionType.STRING, "command", "Command to send to server", true);
//        CommandData reinstall = new CommandData("reinstall", "Reinstalls the current selected server");
//        CommandData rename = new CommandData("rename", "Renames the current server").addOption(OptionType.STRING, "name", "The name to set the current selected server to.", true);
//        api.updateCommands().addCommands(link, servers, server, s, send, reinstall, rename, create, access).queue();


    }
}


