package me.alexirving.pterobot

import com.mattmalec.pterodactyl4j.PteroBuilder
import com.mattmalec.pterodactyl4j.UtilizationState
import com.mattmalec.pterodactyl4j.client.entities.PteroClient
import com.mattmalec.pterodactyl4j.client.entities.Utilization
import me.alexirving.pterobot.struct.Bot
import me.alexirving.pterobot.struct.GuildSetting
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Role
import net.dv8tion.jda.api.events.interaction.command.GenericCommandInteractionEvent
import net.dv8tion.jda.api.events.interaction.component.GenericComponentInteractionCreateEvent
import okhttp3.OkHttpClient
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import kotlin.random.Random

val okHttp = OkHttpClient()

fun buildClientSafely(url: String?, key: String, async: (client: PteroClient?) -> Unit) {
    try {
        val pc = PteroBuilder.create(url, key).setHttpClient(okHttp).buildClient()
        pc.retrieveAccount().executeAsync(
            {
                async(pc)
            },
            {
                async(null)
            }
        )
    } catch (_: IllegalArgumentException) {
        async(null)
    }

}


fun Utilization.getStatus(): String {
    return when (this.state) {
        UtilizationState.STOPPING -> "\uD83D\uDFE0"
        UtilizationState.STARTING -> "\uD83D\uDFE1"
        UtilizationState.RUNNING -> "\uD83D\uDFE2"
        UtilizationState.OFFLINE -> "\uD83D\uDD34"
        else -> "shmezi is cool"
    }
}

fun String.rp(replacements: Map<String, String>) = replacements.entries.fold(this) { acc, (p, v) -> acc.replace(p, v) }

//At some point will clean this up im in a rush dont judge me.
fun validateAdmin(bot: Bot, e: GenericComponentInteractionCreateEvent, withResponses: Boolean = true): Boolean {
    val member = e.member

    fun m(message: String) {
        if (withResponses)
            e.hook.editOriginal(message).queue()
    }
    if (withResponses)
        e.deferReply(true).queue()


    if (member == null) {
        m("Commands must be run inside a guild!")
        return false
    }

    val roleId = bot.getString(member.guild.id, GuildSetting.STAFF)
    var role: Role? = null
    if (roleId != null)
        role = member.guild.getRoleById(roleId)
    if (!member.hasPermission(Permission.ADMINISTRATOR) && (if (role != null) !member.roles.contains(role) else false)) {
        m("You do not have permission to run this command!")
        return false
    }
    return true
}

fun validateAdmin(bot: Bot, e: GenericCommandInteractionEvent, withResponses: Boolean = true): Boolean {
    val member = e.member

    fun m(message: String) {
        if (withResponses)
            e.hook.editOriginal(message).queue()
    }
    if (withResponses)
        e.deferReply(true).queue()


    if (member == null) {
        m("Commands must be run inside a guild!")
        return false
    }

    val roleId = bot.getString(member.guild.id, GuildSetting.STAFF)
    var role: Role? = null
    if (roleId != null)
        role = member.guild.getRoleById(roleId)
    if (!member.hasPermission(Permission.ADMINISTRATOR) && (if (role != null) !member.roles.contains(role) else false)) {
        m("You do not have permission to run this command!")
        return false
    }
    return true
}

fun copyOver(dataFolder: File, vararg fileNames: String) {
    for (name in fileNames) {
        val tc = File(dataFolder, name)
        if (tc.exists())
            continue
        if (name.matches(".+\\..+\$".toRegex()))
            Thread.currentThread().contextClassLoader.getResourceAsStream(name)?.let {
                Files.copy(
                    it,
                    tc.toPath(),
                    StandardCopyOption.REPLACE_EXISTING
                )
            }
        else
            tc.mkdir()

    }
}

fun copyOver(vararg fileNames: String) {
    for (name in fileNames) {
        val tc = File(name)
        if (tc.exists())
            continue
        if (name.matches(".+\\..+\$".toRegex()))
            Files.copy(
                Thread.currentThread().contextClassLoader.getResourceAsStream(name),
                tc.toPath(),
                StandardCopyOption.REPLACE_EXISTING
            )
        else
            tc.mkdir()

    }
}

fun <T> T?.print(): T? {
    println(this)
    return this
}


var c = 0
fun <T> T?.pq(): T? {
    this.pq(null)
    return this
}

fun <T> T?.pqr(): T? {
    pq(Random.nextInt(0, 100))
    return this
}

fun <T> T?.pq(number: Int): T? {
    this.pq("$number")
    return this
}

fun <T> T?.pq(prefix: String?): T? {

    val p = (prefix ?: "PRINTED").apply { replace(this[0], this[0].uppercaseChar()) }
    if (this == null) {
        println("[$p] null".color(Colors.RED))
        return null
    }
    when (c) {
        0 -> println("[$p] $this".color(Colors.RED))
        1 -> println("[$p] $this".color(Colors.BLUE))
        2 -> println("[$p] $this".color(Colors.GREEN))
        3 -> println("[$p] $this".color(Colors.PURPLE))
        4 -> println("[$p] $this".color(Colors.CYAN))
        5 -> println("[$p] $this".color(Colors.YELLOW))
        else -> println("[$p] $this".color(Colors.CYAN))
    }

    c++
    if (c > 5)
        c = 0
    return this
}

fun <T> T?.pq(validate: Validate): T? {
    return this.pq(validate.message)
}

fun <T> T?.pqvn(): T? = this.pq(Validate.NULL)

enum class Validate(val message: String) {
    NULL("Should not be null!")
}
