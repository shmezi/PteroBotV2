package me.alexirving.pterobot.embed

import me.alexirving.pterobot.rp
import net.dv8tion.jda.api.EmbedBuilder

data class RawEmbed(
    val content: String?,
    val embed: Embed
) {
    fun build(p: Map<String, String>) =
        EmbedBuilder().apply {
            if (embed.color != null)
                setColor(embed.color)
            if (embed.thumbnail != null)
                setThumbnail(embed.thumbnail.url)
            if (embed.footer != null)
                setFooter(embed.footer.text?.rp(p), embed.footer.icon_url)
            if (embed.thumbnail != null)
                setThumbnail(embed.thumbnail.url)
            if (embed.description != null)
                setDescription(embed.description.rp(p))
            if (embed.author != null)
                setAuthor(embed.author.name?.rp(p), embed.author.url, embed.author.icon_url)
            if (embed.title != null)
                setTitle(embed.title.rp(p), embed.url)
            if (embed.image != null)
                setImage(embed.image.url)
            if (embed.fields?.isNotEmpty() == true)
                embed.fields.forEach {
                    addField(it.name?.rp(p) ?: "", it.value?.rp(p) ?: "", it.inline ?: false)
                }

        }.build()
}