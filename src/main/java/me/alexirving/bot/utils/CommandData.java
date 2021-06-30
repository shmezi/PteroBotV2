package me.alexirving.bot.utils;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class CommandData {
    public static void updateCommands(Guild guild) {
        net.dv8tion.jda.api.interactions.commands.build.CommandData access = new net.dv8tion.jda.api.interactions.commands.build.CommandData("access", "Add your account as  a sub-user to another user");
        net.dv8tion.jda.api.interactions.commands.build.CommandData create = new net.dv8tion.jda.api.interactions.commands.build.CommandData("create", "Create a server")
                .addOption(OptionType.USER, "owner", "Owner for server", true)
                .addOption(OptionType.STRING, "egg", "Egg for server", true)
                .addOption(OptionType.INTEGER, "ram", "Ram limit for server in MB", true)
                .addOption(OptionType.INTEGER, "cpu", "CPU limit for server in MB", true)
                .addOption(OptionType.INTEGER, "diskspace", "Disk limit for server", true)
                .addOption(OptionType.STRING, "name", "Name for server", true)
                .addOption(OptionType.STRING, "description", "Name for server", false);
        net.dv8tion.jda.api.interactions.commands.build.CommandData staffHelp = new net.dv8tion.jda.api.interactions.commands.build.CommandData("staffhelp", "Open staff-help menu");
        net.dv8tion.jda.api.interactions.commands.build.CommandData help = new net.dv8tion.jda.api.interactions.commands.build.CommandData("help", "Open help menu");
        net.dv8tion.jda.api.interactions.commands.build.CommandData invite = new net.dv8tion.jda.api.interactions.commands.build.CommandData("invite", "Get an invite bot for the bot!");
        net.dv8tion.jda.api.interactions.commands.build.CommandData inv = new net.dv8tion.jda.api.interactions.commands.build.CommandData("inv", "Get an invite bot for the bot!");
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
        net.dv8tion.jda.api.interactions.commands.build.CommandData setup = new net.dv8tion.jda.api.interactions.commands.build.CommandData("setup", "Command to set up the Bot ")
                .addOptions(options)
                .addOption(OptionType.STRING, "to", "Value to change to", true);


        net.dv8tion.jda.api.interactions.commands.build.CommandData link = new net.dv8tion.jda.api.interactions.commands.build.CommandData("link", "Command to link account to pterodactyl.").addOption(OptionType.STRING, "key", "API key from panel", true);
        net.dv8tion.jda.api.interactions.commands.build.CommandData servers = new net.dv8tion.jda.api.interactions.commands.build.CommandData("servers", "Command to get your servers.").addOption(OptionType.USER, "user", "Check a server of another user", false);
        net.dv8tion.jda.api.interactions.commands.build.CommandData server = new net.dv8tion.jda.api.interactions.commands.build.CommandData("server", "Command to get your servers.").addOption(OptionType.USER, "user", "Check a server of another user", false);
        net.dv8tion.jda.api.interactions.commands.build.CommandData s = new net.dv8tion.jda.api.interactions.commands.build.CommandData("s", "Command to get your servers.").addOption(OptionType.USER, "user", "Check a server of another user", false);
        net.dv8tion.jda.api.interactions.commands.build.CommandData send = new net.dv8tion.jda.api.interactions.commands.build.CommandData("send", "Send a command to the current selected server!").addOption(OptionType.STRING, "command", "Command to send to server", true);
        net.dv8tion.jda.api.interactions.commands.build.CommandData reinstall = new net.dv8tion.jda.api.interactions.commands.build.CommandData("reinstall", "Reinstalls the current selected server");
        net.dv8tion.jda.api.interactions.commands.build.CommandData rename = new net.dv8tion.jda.api.interactions.commands.build.CommandData("rename", "Renames the current server").addOption(OptionType.STRING, "name", "The name to set the current selected server to.", true);
        guild.updateCommands().addCommands(link, servers, server, s, send, reinstall, create, rename, access, setup, staffHelp, help, inv, invite).queue();
    }
}
