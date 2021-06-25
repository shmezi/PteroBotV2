package me.alexirivng.bot;

import com.mattmalec.pterodactyl4j.client.entities.ClientServer;
import com.mattmalec.pterodactyl4j.client.entities.PteroClient;
import com.mattmalec.pterodactyl4j.client.entities.Utilization;
import me.alexirivng.bot.utils.Messages;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

import java.util.Objects;

public class Utils {
    public static Utilization getState(PteroClient server, ClientServer clientServer) {
        return server.retrieveUtilization(clientServer).execute();
    }
    public static String getConfigMessage(String guild,Messages messageType){
        if (Objects.isNull(DiscordBot.getGuildConfiguration.get(guild,messageType))){
            return DiscordBot.getGuildConfiguration.get("DEFAULT",messageType);
        }else{
            return DiscordBot.getGuildConfiguration.get(guild,messageType);
        }
    }

    public static String getIsOWner(ClientServer server) {
        if (server.isServerOwner()) {
            return "\uD83D\uDC51";

        } else {
            return "\uD83D\uDC68\u200D\uD83D\uDC67\u200D\uD83D\uDC66";
        }
    }
    public static void updateActivity(JDA api){
        api.getPresence().setActivity(Activity.watching("over " + DiscordBot.memberTokens.rowKeySet().size()+ " users servers!"));
        api.getPresence().setStatus(OnlineStatus.DO_NOT_DISTURB);
    }

    public static String getStatus(Utilization utilization) {
        switch (utilization.getState()) {
            case STOPPING:
                return "\uD83D\uDFE0";
            case STARTING:
                return "\uD83D\uDFE1";
            case RUNNING:
                return "\uD83D\uDFE2";

            case OFFLINE:
                return "\uD83D\uDD34";
            default:
                return "⚪";
        }
    }


    public static boolean isLinked(Member member) {
        //Checking if the user has a linked account.
        if (!Objects.isNull(DiscordBot.memberTokens.get(member.getId(), member.getGuild().getId()))) {
            return true;
        } else {
            return false;

        }
    }
}
