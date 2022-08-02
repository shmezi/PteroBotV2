package me.alexirving.pterobot.bot.cmds

import dev.triumphteam.cmd.core.BaseCommand
import dev.triumphteam.cmd.core.annotation.Command
import dev.triumphteam.cmd.core.annotation.Default
import dev.triumphteam.cmd.slash.sender.SlashSender
import me.alexirving.pterobot.struct.Bot

@Command("test")
class Test(private val bot: Bot) : BaseCommand() {


    @Default
    fun test(e: SlashSender) {
        e.deferReply().queue()
        val s = "{\n" +
                "    \"content\": \"You can~~not~~ do `this`.```py\\nAnd this.\\nprint('Hi')```\\n*italics* or _italics_     __*underline italics*__\\n**bold**     __**underline bold**__\\n***bold italics***  __***underline bold italics***__\\n__underline__     ~~Strikethrough~~\",\n" +
                "    \"embed\": {\n" +
                "        \"title\": \"Hello ~~people~~ world :wave:\",\n" +
                "        \"description\": \"You can use [links](https://discord.com) or emojis :smile: \uD83D\uDE0E\\n```\\nAnd also code blocks\\n```\",\n" +
                "        \"color\": 4321431,\n" +
                "        \"timestamp\": \"2022-07-09T06:17:02.030Z\",\n" +
                "        \"url\": \"https://discord.com\",\n" +
                "        \"author\": {\n" +
                "            \"name\": \"Author name\",\n" +
                "            \"url\": \"https://discord.com\",\n" +
                "            \"icon_url\": \"https://unsplash.it/100\"\n" +
                "        },\n" +
                "        \"thumbnail\": {\n" +
                "            \"url\": \"https://unsplash.it/200\"\n" +
                "        },\n" +
                "        \"image\": {\n" +
                "            \"url\": \"https://unsplash.it/380/200\"\n" +
                "        },\n" +
                "        \"footer\": {\n" +
                "            \"text\": \"Footer text\",\n" +
                "            \"icon_url\": \"https://unsplash.it/100\"\n" +
                "        },\n" +
                "        \"fields\": [\n" +
                "            {\n" +
                "                \"name\": \"Field 1, *lorem* **ipsum**, ~~dolor~~\",\n" +
                "                \"value\": \"Field value\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"name\": \"Field 2\",\n" +
                "                \"value\": \"You can use custom emojis <:Kekwlaugh:722088222766923847>. <:GangstaBlob:742256196295065661>\",\n" +
                "                \"inline\": false\n" +
                "            },\n" +
                "            {\n" +
                "                \"name\": \"Inline field\",\n" +
                "                \"value\": \"Fields can be inline\",\n" +
                "                \"inline\": true\n" +
                "            },\n" +
                "            {\n" +
                "                \"name\": \"Inline field\",\n" +
                "                \"value\": \"*Lorem ipsum*\",\n" +
                "                \"inline\": true\n" +
                "            },\n" +
                "            {\n" +
                "                \"name\": \"Inline field\",\n" +
                "                \"value\": \"value\",\n" +
                "                \"inline\": true\n" +
                "            },\n" +
                "            {\n" +
                "                \"name\": \"Another field\",\n" +
                "                \"value\": \"> Nope, didn't forget about this\",\n" +
                "                \"inline\": false\n" +
                "            }\n" +
                "        ]\n" +
                "    }\n" +
                "}"
    }
}