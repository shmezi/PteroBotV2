package me.alexirving.bot.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.components.Button;

import java.awt.*;

public class Invite {
    public static void inviteCommand(SlashCommandEvent event){
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("Invite link:");
        builder.setColor(Color.PINK);
        builder.setFooter("Bot created by Alex irving shmezi#6969");
        builder.setThumbnail("https://cdn.discordapp.com/attachments/858782083857645569/859023750091898900/bot.png");
        event.getHook().editOriginalEmbeds(builder.build()).setActionRow(
                Button.link("https://discord.com/oauth2/authorize?client_id="+ event.getJDA().getSelfUser().getId() + "&scope=bot&permissions=76800&scope=bot+applications.commands","INVITE ME!")
        ).queue();

    }
}
