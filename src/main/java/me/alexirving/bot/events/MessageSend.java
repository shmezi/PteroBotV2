package me.alexirving.bot.events;

import me.alexirving.bot.utils.Utils;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class MessageSend extends ListenerAdapter {
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getMessage().getContentRaw().equals("-reload")) {
            if (event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
                Utils.updateCommands(event.getGuild());
                event.getMessage().reply("Reloaded!").queue();
            }
        }
    }
}
