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


    public static boolean isLinkValid(String url) {
        try {
            new URL(url).toURI();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
