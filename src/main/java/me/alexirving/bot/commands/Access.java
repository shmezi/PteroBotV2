package me.alexirving.bot.commands;

import com.mattmalec.pterodactyl4j.Permission;
import com.mattmalec.pterodactyl4j.PteroBuilder;
import com.mattmalec.pterodactyl4j.client.entities.PteroClient;
import me.alexirving.bot.Database;
import me.alexirving.bot.ServerManager;
import me.alexirving.bot.utils.Messages;
import me.alexirving.bot.utils.Utils;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import java.util.Objects;

public class Access {
    public void accessCommand(SlashCommandEvent event) {
        String guildId = event.getGuild().getId();
        String memberId = event.getMember().getId();
        if (Utils.isStaff(event.getMember())) {
            if (Utils.isLinked(event.getMember())) {
                if (!Objects.isNull(Database.memberServers.get(guildId, memberId)) || !Objects.isNull(Database.panelSessionPageNumber.get(guildId, memberId))) {
                    if (Utils.isLinkValid(Utils.getConfigMessage(guildId, Messages.API_URL))){
                        PteroClient client = PteroBuilder.createClient(Utils.getConfigMessage(guildId, Messages.API_URL), Database.memberTokens.get(guildId, memberId));
                        if (!ServerManager.getCurrentServer(event.getMember()).getSubuser(ServerManager.getCurrentServer(event.getMember()).getUUID()).isPresent()) {
                            if (!ServerManager.getCurrentServer(event.getMember()).isServerOwner()) {
                                ServerManager.getCurrentServer(event.getMember()).getSubuserManager().createUser().setEmail(client.retrieveAccount().execute().getEmail()).setPermissions(Permission.ALL_PERMISSIONS).executeAsync();
                                event.getHook().editOriginal(Utils.getConfigMessage(guildId, Messages.ADDED_TO_SERVER)).queue();
                            } else {
                                System.out.println("test1");
                                event.getHook().editOriginal(Utils.getConfigMessage(guildId, Messages.OWNER_ON_SERVER)).queue();
                            }

                        } else {
                            event.getHook().editOriginal(Utils.getConfigMessage(guildId, Messages.SUB_USER_ON_SERVER)).queue();
                        }
                    }else{
                        event.getHook().editOriginal("API URL NOT SETUP CORRECTLY!").queue();
                    }

                } else {
                    System.out.println("test2");
                    event.getHook().editOriginal(Utils.getConfigMessage(guildId, Messages.NO_SELECTED_SERVER)).queue();
                }
            } else {
                System.out.println("test3");
                event.getHook().editOriginal(Utils.getConfigMessage(guildId, Messages.NOT_LINKED)).queue();
            }
        } else {
            System.out.println("test4");
            event.getHook().editOriginal(Utils.getConfigMessage(guildId, Messages.NO_PERMISSION)).queue();
        }
    }
}
