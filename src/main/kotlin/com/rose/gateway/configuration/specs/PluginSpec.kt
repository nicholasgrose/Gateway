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
        const val DEFAULT_PRIMARY_COLOR = "#56EE5C"
        const val DEFAULT_SECONDARY_COLOR = "#7289DA"
        const val DEFAULT_TERTIARY_COLOR = "#F526ED"
        const val DEFAULT_WARNING_COLOR = "#EB4325"

        val primaryColor by optional(
            DEFAULT_PRIMARY_COLOR,
            description = "Used for labels and Discord mentions in-game."
        )
        val secondaryColor by optional(
            DEFAULT_SECONDARY_COLOR,
            description = "Used for differentiated text elements and names of Discord users in-game."
        )
        val tertiaryColor by optional(
            DEFAULT_TERTIARY_COLOR,
            description = "Used for labelling configuration paths."
        )
        val warningColor by optional(
            DEFAULT_WARNING_COLOR,
            description = "Used for marking configurations that can be null."
        )
    }
}
