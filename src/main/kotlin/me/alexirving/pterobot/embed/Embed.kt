package me.alexirving.pterobot.embed

import java.time.Instant
import java.time.format.DateTimeFormatter

data class Embed(
    val author: Author?,
    val color: Int?,
    val description: String?,
    val fields: List<Field>?,
    val footer: Footer?,
    val image: Image?,
    val thumbnail: Thumbnail?,
    val title: String?,
    val url: String?
)