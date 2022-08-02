package me.alexirving.pterobot.struct

import com.mattmalec.pterodactyl4j.DataType
import com.mattmalec.pterodactyl4j.Permission
import com.mattmalec.pterodactyl4j.client.entities.ClientServer
import com.mattmalec.pterodactyl4j.client.entities.PteroClient
import com.mattmalec.pterodactyl4j.client.entities.Utilization
import me.alexirving.pterobot.getStatus
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.interactions.InteractionHook
import net.dv8tion.jda.api.interactions.components.ActionRow
import net.dv8tion.jda.api.interactions.components.buttons.Button
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu
import net.dv8tion.jda.api.interactions.components.selections.SelectOption

class PanelSession(
    private val bot: Bot,
    private val client: PteroClient,
    private val member: Member,
    private val hook: InteractionHook
) {

    private val cached = mutableMapOf<ClientServer, Utilization>()
    private lateinit var current: ClientServer
    fun reloadCache(async: () -> Unit) {
        client.retrieveServers().forEachAsync { server ->
            server.retrieveUtilization().executeAsync {
                cached[server] = it
            }
            true
        }.thenAccept {
            current = cached.keys.firstOrNull() ?: return@thenAccept
            async()
        }
    }

    private fun Boolean.emoji(): String {
        return if (this) "\uD83D\uDC51" else "\uD83D\uDC6A"
    }

    fun updatePanel(first: Boolean = false) {
        hook.editOriginalEmbeds(
            bot.getEmbed(member.guild.id, GuildSetting.PANEL_TEMPLATE)?.build(
                mutableMapOf<String, String>().apply {
                    this["%servers%"] = formatServers()
                    this["%user%"] = member.user.name
                    this["%selected_name%"] = current.name
                    this["%selected_ram_mb%"] = cached[current]?.getMemoryFormatted(DataType.MB) ?: "Null"
                    this["%selected_storage_gb%"] = cached[current]?.getDiskFormatted(DataType.GB) ?: "Null"
                    this["%selected_cpu_mb%"] = cached[current]?.cpu.toString()

                })
        ).queue()
        if (first) {
            val l = mutableListOf(
                ActionRow.of(
                    SelectMenu.create(hook.interaction.user.id).setPlaceholder("Select a server to control!")
                        .addOptions(
                            cached.keys.map {
                                SelectOption.of(
                                    "${it.isServerOwner.emoji()} | ${it.name}",
                                    it.identifier
                                )
                            }
                        ).build()
                ),
                ActionRow.of(
                    Button.success("${hook.interaction.user.id}-start", "Start"),
                    Button.secondary("${hook.interaction.user.id}-restart", "Restart"),
                    Button.danger("${hook.interaction.user.id}-stop", "Stop"),
                    Button.primary("${hook.interaction.user.id}-cmd", "Command")
                )
            )
            if (member.user != hook.interaction.user) {
                l.add(ActionRow.of(Button.primary("${hook.interaction.user.id}-access", "Access selected server")))
            }
            hook.editOriginalComponents(l).queue()
        }
    }

    private fun formatServers(): String {
        val g = member.guild
        val b = StringBuilder()
        var f = true
        cached.forEach {
            val s = if (f) "" else "\n"
            b.append(
                "$s[${it.key.isServerOwner.emoji()} ${it.key.name} - ${it.value.getStatus()}](${
                    "${bot.getString(g.id, GuildSetting.URL)}server/${it.key.identifier}"
                })"
            )
            f = false
        }
        return b.toString()
    }

    var first = true

    fun select(id: String) {
        current = cached.keys.first { it.identifier == id }
        updatePanel()
    }

    fun start() = current.start().executeAsync()
    fun stop() = current.stop().executeAsync()
    fun reStart() = current.restart().executeAsync()
    fun sendCommand(command: String) = current.sendCommand(command).executeAsync()
    fun access(email: String) {
        current.subuserManager.createUser().setEmail(email).setPermissions(
            Permission.CONTROL_CONSOLE,
            Permission.CONTROL_RESTART,
            Permission.CONTROL_START,
            Permission.CONTROL_STOP,
            Permission.DATABASE_READ,
            Permission.DATABASE_CREATE,
            Permission.DATABASE_UPDATE,
            Permission.SCHEDULE_READ,
            Permission.SCHEDULE_CREATE,
            Permission.SCHEDULE_UPDATE,
            Permission.USER_READ,
            Permission.BACKUP_READ,
            Permission.BACKUP_CREATE,
            Permission.BACKUP_UPDATE,
            Permission.BACKUP_DOWNLOAD,
            Permission.BACKUP_RESTORE,
            Permission.FILE_READ,
            Permission.FILE_READ_CONTENT,
            Permission.FILE_CREATE,
            Permission.FILE_UPDATE,
            Permission.FILE_ARCHIVE,
            Permission.SETTINGS_RENAME,
            Permission.SETTINGS_REINSTALL
        ).executeAsync()
    }
}

