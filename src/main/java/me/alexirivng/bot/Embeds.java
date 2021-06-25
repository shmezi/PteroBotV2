package me.alexirivng.bot;

import me.alexirivng.bot.utils.EmbedTypes;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class Embeds {
    public MessageEmbed makeEmbed(EmbedTypes type) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(11393254);
        switch (type) {

            case LINKED:
                builder.setTitle("**Account linked!**");
                break;

        }
        return builder.build();
    }
}
