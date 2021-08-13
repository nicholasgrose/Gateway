package com.rose.gateway.configuration

import com.uchuhimo.konf.ConfigSpec

object PluginSpec : ConfigSpec() {
    val botToken by required<String>(
        description = "The token used by the bot to access discord. Accessible at https://discord.com/developers/applications/."
    )

    object BotSpec : ConfigSpec() {
        val commandPrefix by optional("!", description = "The prefix used to mark commands. Defaults to '!'.")
        val commandTimeout by optional(
            5_000,
            description = "The length of time in milliseconds that commands live for before being cleared."
        )
        val botChannels by optional(
            listOf<String>(),
            description = "The names of the channels in which the bot should respond to commands and post/accept chat messages."
        )
        val memberQueryMax by optional(
            100,
            description = "The maximum number of users that can be queried for when searching for a user by name."
        )

        object ExtensionsSpec : ConfigSpec() {
            object WhitelistSpec : CommonExtensionSpec()
            object ListSpec : CommonExtensionSpec()
            object AboutSpec : CommonExtensionSpec()
            object ChatSpec : CommonExtensionSpec()
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
