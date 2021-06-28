package me.alexirving.bot.utils;

import com.mattmalec.pterodactyl4j.client.entities.ClientServer;
import com.mattmalec.pterodactyl4j.client.entities.PteroClient;
import com.mattmalec.pterodactyl4j.client.entities.Utilization;
import me.alexirving.bot.Database;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.net.URL;
import java.util.Objects;

public class Utils {
    public static Utilization getState(PteroClient server, ClientServer clientServer) {
        return server.retrieveUtilization(clientServer).execute();
    }

    public static String getConfigMessage(String guild, Messages messageType) {
        if (Objects.isNull(Database.getGuildConfiguration.get(guild, messageType))) {
            return Database.getGuildConfiguration.get("DEFAULT", messageType);
        } else {
            return Database.getGuildConfiguration.get(guild, messageType);
        }
    }

    public static boolean isLong(String number) {
        try {
            Long.parseLong(number);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    public static boolean isStaff(Member member) {
        Guild guild = member.getGuild();
        if (isLong(Utils.getConfigMessage(guild.getId(), Messages.STAFF_ID))) {
            if (!Objects.isNull(guild.getRoleById(Utils.getConfigMessage(guild.getId(), Messages.STAFF_ID)))) {
                if (member.getRoles().contains(guild.getRoleById(Utils.getConfigMessage(guild.getId(), Messages.STAFF_ID)))) {
                    return true;
                } else {
                    return false;
                }
            } else {
                System.out.println("#1 STAFF ROLE NOT SET UP CORRECTLY ON GUILD: " + guild.getId() + "!");
                return false;
            }
        } else {
            System.out.println("#1 STAFF ROLE NOT SET UP CORRECTLY ON GUILD: " + guild.getId() + "!");

            return false;
        }

    }


    public static String isOwner(ClientServer server) {
        if (server.isServerOwner()) {
            return "\uD83D\uDC51";

        } else {
            return "\uD83D\uDC68\u200D\uD83D\uDC67\u200D\uD83D\uDC66";
        }
    }

    public static void updateActivity(JDA api) {
        api.getPresence().setActivity(Activity.watching("over " + Database.memberTokens.rowMap().size() + " Ptero panels!"));
    }

    public static String getStatus(Utilization utilization) {
        return switch (utilization.getState()) {
            case STOPPING -> "\uD83D\uDFE0";
            case STARTING -> "\uD83D\uDFE1";
            case RUNNING -> "\uD83D\uDFE2";
            case OFFLINE -> "\uD83D\uDD34";
        };
    }


    public static boolean isLinked(Member member) {
        //Checking if the user has a linked account.
        return !Objects.isNull(Database.memberTokens.get(member.getGuild().getId(), member.getId()));
    }

    public static void updateCommands(Guild guild) {
        CommandData access = new CommandData("access", "Add your account as  a sub-user to another user");
        CommandData create = new CommandData("create", "Create a server")
                .addOption(OptionType.USER, "owner", "Owner for server", true)
                .addOption(OptionType.STRING, "egg", "Egg for server", true)
                .addOption(OptionType.INTEGER, "ram", "Ram limit for server in MB", true)
                .addOption(OptionType.INTEGER, "cpu", "CPU limit for server in MB", true)
                .addOption(OptionType.INTEGER, "diskspace", "Disk limit for server", true)
                .addOption(OptionType.STRING, "name", "Name for server", true)
                .addOption(OptionType.STRING, "description", "Name for server", false);
        CommandData staffHelp = new CommandData("staffhelp", "Open staff-help menu");
        CommandData help = new CommandData("help", "Open help menu");

        Command.Choice API_URL = new Command.Choice("[API_URL] The URL for your panel.", "API_URL");
        Command.Choice STAFF_ID = new Command.Choice("[STAFF_ID] The staff role will be able to interact with user's servers.", "STAFF_ID");
        Command.Choice NOT_LINKED = new Command.Choice("[NOT_LINKED] The message sent when a user is not linked.", "NOT_LINKED");
        Command.Choice INVALID_APIKEY = new Command.Choice("[INVALID_APIKEY] The message sent when a user sent an invalid linking API key.", "INVALID_APIKEY");
        Command.Choice LINKED = new Command.Choice("[LINKED] The message sent when a user links.", "LINKED");
        Command.Choice PANEL_AVATAR_URL = new Command.Choice("[PANEL_AVATAR_URL] The URL to an image to be displayed in the top embed of the server manager.", "PANEL_AVATAR_URL");
        Command.Choice INFO_AVATAR_URL = new Command.Choice("[INFO_AVATAR_URL] The URL to an image to be displayed in the bottom embed of the server manager.", "INFO_AVATAR_URL");
        Command.Choice NO_SERVER = new Command.Choice("[NO_SERVER] The message sent when a user has no servers.", "NO_SERVER");
        Command.Choice COMMAND_SENT = new Command.Choice("[COMMAND_SENT] The message sent when a user has no servers.", "COMMAND_SENT");
        Command.Choice RENAMED_SERVER = new Command.Choice("[RENAMED_SERVER] The message sent when renaming a server.", "RENAMED_SERVER");
        Command.Choice REINSTALL_VERIFY = new Command.Choice("[REINSTALL_VERIFY] The confirmation message for reinstalling a server.", "REINSTALL_VERIFY");
        Command.Choice REINSTALL = new Command.Choice("[REINSTALL] The message sent after confirmation of server reinstall.", "REINSTALL");
        Command.Choice NO_PERMISSION = new Command.Choice("[NO_PERMISSION] The message sent when a user doesn't have permission for something.", "NO_PERMISSION");
        Command.Choice ADDED_TO_SERVER = new Command.Choice("[ADDED_TO_SERVER] The message sent being added as a sub-user in the /access.", "ADDED_TO_SERVER");
        Command.Choice SUB_USER_ON_SERVER = new Command.Choice("[SUB_USER_ON_SERVER] The message sent when already a sub-user in /access.", "SUB_USER_ON_SERVER");
        Command.Choice OWNER_ON_SERVER = new Command.Choice("[OWNER_ON_SERVER] The message sent when the user is the owner in /access.", "OWNER_ON_SERVER");
        Command.Choice NO_SELECTED_SERVER = new Command.Choice("[NO_SELECTED_SERVER] The message sent when a user runs a command but no server is selected.", "NO_SELECTED_SERVER");
        Command.Choice HELP_AVATAR_URL = new Command.Choice("[HELP_AVATAR_URL] The image URL for the help menu embed.", "HELP_AVATAR_URL");
        OptionData options = new OptionData(OptionType.STRING, "value", "to", true).addChoices(API_URL, STAFF_ID, NOT_LINKED, INVALID_APIKEY, LINKED, PANEL_AVATAR_URL,
                INFO_AVATAR_URL, NO_SERVER, COMMAND_SENT, RENAMED_SERVER, REINSTALL_VERIFY, REINSTALL, NO_PERMISSION, ADDED_TO_SERVER, SUB_USER_ON_SERVER, OWNER_ON_SERVER, NO_SELECTED_SERVER, HELP_AVATAR_URL);
        CommandData setup = new CommandData("setup", "Command to set up the Bot ")
                .addOptions(options)
                .addOption(OptionType.STRING, "to", "Value to change to", true);


        CommandData link = new CommandData("link", "Command to link account to pterodactyl.").addOption(OptionType.STRING, "key", "API key from panel", true);
        CommandData servers = new CommandData("servers", "Command to get your servers.").addOption(OptionType.USER, "user", "Check a server of another user", false);
        CommandData server = new CommandData("server", "Command to get your servers.").addOption(OptionType.USER, "user", "Check a server of another user", false);
        CommandData s = new CommandData("s", "Command to get your servers.").addOption(OptionType.USER, "user", "Check a server of another user", false);
        CommandData send = new CommandData("send", "Send a command to the current selected server!").addOption(OptionType.STRING, "command", "Command to send to server", true);
        CommandData reinstall = new CommandData("reinstall", "Reinstalls the current selected server");
        CommandData rename = new CommandData("rename", "Renames the current server").addOption(OptionType.STRING, "name", "The name to set the current selected server to.", true);
        guild.updateCommands().addCommands(link, servers, server, s, send, reinstall, create, rename, access, setup, staffHelp, help).queue();
    }
    public static boolean isLinkValid(String url){
        try { new URL(url).toURI();
            return true; }
        catch (Exception e) {
            return false;
        }
    }
}
