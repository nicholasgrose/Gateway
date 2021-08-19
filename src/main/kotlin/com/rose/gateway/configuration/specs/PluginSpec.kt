package com.rose.gateway.configuration.specs

import com.rose.gateway.bot.extensions.about.AboutExtension
import com.rose.gateway.bot.extensions.chat.ChatExtension
import com.rose.gateway.bot.extensions.list.ListExtension
import com.rose.gateway.bot.extensions.whitelist.WhitelistExtension
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
            object AboutSpec : CommonExtensionSpec(AboutExtension.extensionName())
            object ChatSpec : CommonExtensionSpec(ChatExtension.extensionName())
            object ListSpec : CommonExtensionSpec(ListExtension.extensionName())
            object WhitelistSpec : CommonExtensionSpec(WhitelistExtension.extensionName())
        }
    }


    object MinecraftSpec : ConfigSpec() {
        val primaryColor by optional(
            "#56EE5C",
            description = "Used for Discord mentions and labels in-game."
        )
        val secondaryColor by optional(
            "#7289DA",
            description = "Used for names of Discord users in-game."
        )
        val tertiaryColor by optional(
            "#F526ED",
            description = "Used for labelling configurations."
        )
        val warningColor by optional(
            "#EB4325",
            description = "Used for marking configurations that can be null."
        )
    }
}
