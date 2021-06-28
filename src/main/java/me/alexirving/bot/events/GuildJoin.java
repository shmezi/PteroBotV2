package me.alexirving.bot.events;

import me.alexirving.bot.utils.Utils;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class GuildJoin extends ListenerAdapter {
    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        Utils.updateCommands(event.getGuild());
        System.out.println("I just joined " + event.getGuild().getName());


    }
}
