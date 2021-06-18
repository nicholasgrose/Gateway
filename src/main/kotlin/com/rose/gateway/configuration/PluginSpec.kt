package com.rose.gateway.configuration

import com.uchuhimo.konf.ConfigSpec

object PluginSpec : ConfigSpec() {
    val botToken by required<String>(
        description = "The token used by the bot to access discord. Accessible at https://discord.com/developers/applications/."
    )

    object BotSpec : ConfigSpec() {
        val commandPrefix by optional("!", description = "The prefix used to mark commands.")
        val commandTimeout by optional(
            5_000.toLong(),
            description = "The length of time that commands live for before being cleared."
        )
        val botChannels by required<List<String>>(
            description = "The names of the channels in which the bot should respond to commands and post/accept chat messages."
        )
        val memberQueryMax by optional(
            100,
            description = "The max number of users that can be queried when search for a user."
        )

        object EnabledExtensionsSpec : ConfigSpec() {
            val whitelistExtension by optional(true, description = "Whether the whitelist extension should be enabled.")
            val listExtension by optional(true, description = "Whether the player list extension should be enabled.")
            val aboutExtension by optional(true, description = "Whether the about extension should be enabled.")
            val chatExtension by optional(true, description = "Whether the chat extension should be enabled.")
        }
    }

    object MinecraftSpec : ConfigSpec() {
        val discordUserColor by optional(
            "#7289DA",
            description = "The hex color of Discord users as they appear in Minecraft."
        )
        val discordMentionColor by optional(
            "#56EE5C",
            description = "The hex color of Discord mentions as they appear in Minecraft."
        )
    }
}
