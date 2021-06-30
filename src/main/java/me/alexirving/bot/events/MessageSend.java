package me.alexirving.bot.events;

import me.alexirving.bot.Database;
import me.alexirving.bot.utils.CommandData;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class MessageSend extends ListenerAdapter {
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getMessage().getContentRaw().equals("-reload")) {
            if (event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
                CommandData.updateCommands(event.getGuild());
                event.getMessage().reply("Reloaded!").queue();
            }
        } else if (event.getMessage().getContentRaw().equals("-test")) {
        }
        if (!event.getMessage().getAttachments().isEmpty() && event.getMessage().getContentRaw().equals("-config 32")) {

            try {
                System.out.println(new Database().getValue("test", event.getMessage().getAttachments().get(0).downloadToFile().get().getAbsoluteFile()));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
}
