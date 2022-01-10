package com.rose.gateway.configuration.specs

import com.rose.gateway.GatewayPlugin
import com.rose.gateway.bot.extensions.about.AboutExtension
import com.rose.gateway.bot.extensions.chat.ChatExtension
import com.rose.gateway.bot.extensions.ip.IpExtension
import com.rose.gateway.bot.extensions.list.ListExtension
import com.rose.gateway.bot.extensions.whitelist.WhitelistExtension
import com.uchuhimo.konf.ConfigSpec
import kotlinx.coroutines.runBlocking

object PluginSpec : ConfigSpec(), ResponsiveSpec {
    val botToken by required<String>(
        description = "The token used by the bot to access discord." +
            "Accessible at https://discord.com/developers/applications/."
    )

    object BotSpec : ConfigSpec(), ResponsiveSpec {
        val botChannels by optional(
            listOf<String>(),
            description = "The names of the channels in which the bot" +
                " should respond to commands and post/accept chat messages."
        )
        val memberQueryMax by optional(
            1,
            description = "The maximum number of users that can be queried for when searching for a user by name."
        )

        object ExtensionsSpec : ConfigSpec(), ResponsiveSpec {
            object AboutSpec : CommonExtensionSpec(AboutExtension.extensionName()) {
                override fun setConfigChangeActions(plugin: GatewayPlugin) {
                    this.enabled.afterSet { _, value -> modifyExtensionLoadedStatus(value, plugin) }
                }
            }

            object ChatSpec : CommonExtensionSpec(ChatExtension.extensionName()) {
                override fun setConfigChangeActions(plugin: GatewayPlugin) {
                    this.enabled.afterSet { _, value -> modifyExtensionLoadedStatus(value, plugin) }
                }
            }

            object IpSpec : CommonExtensionSpec(IpExtension.extensionName()) {
                val displayIp by required<String>()

                override fun setConfigChangeActions(plugin: GatewayPlugin) {
                    this.enabled.afterSet { _, value -> modifyExtensionLoadedStatus(value, plugin) }
                }
            }

            object ListSpec : CommonExtensionSpec(ListExtension.extensionName()) {
                override fun setConfigChangeActions(plugin: GatewayPlugin) {
                    this.enabled.afterSet { _, value -> modifyExtensionLoadedStatus(value, plugin) }
                }
            }

            object WhitelistSpec : CommonExtensionSpec(WhitelistExtension.extensionName()) {
                override fun setConfigChangeActions(plugin: GatewayPlugin) {
                    this.enabled.afterSet { _, value -> modifyExtensionLoadedStatus(value, plugin) }
                }
            }

            override fun setConfigChangeActions(plugin: GatewayPlugin) {
                AboutSpec.setConfigChangeActions(plugin)
                ChatSpec.setConfigChangeActions(plugin)
                ListSpec.setConfigChangeActions(plugin)
                WhitelistSpec.setConfigChangeActions(plugin)
            }
        }

        override fun setConfigChangeActions(plugin: GatewayPlugin) {
            botChannels.afterSet { _, _ -> runBlocking { plugin.discordBot.fillBotChannels() } }
            ExtensionsSpec.setConfigChangeActions(plugin)
        }
    }

    object MinecraftSpec : ConfigSpec(), ResponsiveSpec {
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

        override fun setConfigChangeActions(plugin: GatewayPlugin) {
            // This does not need to do anything at the moment because colors are always read anew when used.
        }
    }

    override fun setConfigChangeActions(plugin: GatewayPlugin) {
        botToken.afterSet { _, _ -> plugin.restartBot() }
        BotSpec.setConfigChangeActions(plugin)
        MinecraftSpec.setConfigChangeActions(plugin)
    }
}
