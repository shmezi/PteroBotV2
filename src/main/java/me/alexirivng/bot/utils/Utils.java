package me.alexirivng.bot.utils;

import com.mattmalec.pterodactyl4j.client.entities.ClientServer;
import com.mattmalec.pterodactyl4j.client.entities.PteroClient;
import com.mattmalec.pterodactyl4j.client.entities.Utilization;
import me.alexirivng.bot.DiscordBot;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Member;

import java.util.Objects;

public class Utils {
    public static Utilization getState(PteroClient server, ClientServer clientServer) {
        return server.retrieveUtilization(clientServer).execute();
    }

    public static String getConfigMessage(String guild, Messages messageType) {
        if (Objects.isNull(DiscordBot.getGuildConfiguration.get(guild, messageType.toString()))) {
            return DiscordBot.getGuildConfiguration.get("DEFAULT", messageType.toString());
        } else {
            return DiscordBot.getGuildConfiguration.get(guild, messageType.toString());
        }
    }

    public static String getIsOwner(ClientServer server) {
        if (server.isServerOwner()) {
            return "\uD83D\uDC51";

        } else {
            return "\uD83D\uDC68\u200D\uD83D\uDC67\u200D\uD83D\uDC66";
        }
    }

    public static void updateActivity(JDA api) {
        api.getPresence().setActivity(Activity.watching("over " + DiscordBot.memberTokens.rowMap().size() + " Ptero panels!"));
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
        return !Objects.isNull(DiscordBot.memberTokens.get(member.getGuild().getId(), member.getId()));
    }
}
