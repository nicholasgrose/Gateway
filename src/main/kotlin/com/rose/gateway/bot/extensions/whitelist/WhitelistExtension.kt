package com.rose.gateway.bot.extensions.whitelist

import com.kotlindiscord.kord.extensions.commands.application.slash.publicSubCommand
import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.publicSlashCommand
import com.rose.gateway.GatewayPlugin
import com.rose.gateway.Logger
import com.rose.gateway.bot.extensions.ToggleableExtension
import com.rose.gateway.bot.message.MessageLifetime.createMessageWithLifetime
import com.rose.gateway.configuration.specs.PluginSpec
import com.rose.gateway.minecraft.whitelist.Whitelist
import com.rose.gateway.minecraft.whitelist.WhitelistState
import com.rose.gateway.shared.configurations.BotConfiguration.commandTimeout

class WhitelistExtension : Extension() {
    companion object : ToggleableExtension {
        override fun extensionName(): String {
            return "whitelist"
        }

        override fun extensionConstructor(): () -> Extension {
            return ::WhitelistExtension
        }

        override fun isEnabled(plugin: GatewayPlugin): Boolean {
            val enabledSpec = PluginSpec.BotSpec.ExtensionsSpec.WhitelistSpec.enabled
            return plugin.configuration[enabledSpec]
        }
    }

    override val name: String = extensionName()
    val plugin = bot.getKoin().get<GatewayPlugin>()
    private val timeout = plugin.configuration.commandTimeout()
    private val whitelistManager = Whitelist(plugin)

    override suspend fun setup() {
        publicSlashCommand {
            name = "whitelist"
            description = "Runs an operation that relates to the server whitelist."

            publicSubCommand(::WhitelistArguments) {
                name = "add"
                description = "Adds a player to the whitelist."

                action {
                    Logger.log("${user.asUserOrNull()?.username} added ${arguments.username} to whitelist!")
                    val status = when (whitelistManager.addToWhitelist(arguments.username)) {
                        WhitelistState.STATE_MODIFIED -> "${arguments.username} successfully added to whitelist."
                        WhitelistState.STATE_SUSTAINED -> "${arguments.username} already exists in whitelist."
                        WhitelistState.STATE_INVALID -> "An error occurred adding ${arguments.username} to whitelist."
                    }
                    channel.createMessageWithLifetime(timeout) {
                        content = status
                    }
                }
            }

            publicSubCommand(::WhitelistArguments) {
                name = "remove"
                description = "Removes a player from the whitelist."

                action {
                    Logger.log("${user.asUserOrNull()?.username} removed ${arguments.username} from whitelist!")
                    val status = when (whitelistManager.removeFromWhitelist(arguments.username)) {
                        WhitelistState.STATE_MODIFIED -> "${arguments.username} successfully removed from whitelist."
                        WhitelistState.STATE_SUSTAINED -> "${arguments.username} does not exist in whitelist."
                        WhitelistState.STATE_INVALID -> "An error occurred removing ${arguments.username} from whitelist."
                    }
                    channel.createMessageWithLifetime(timeout) {
                        content = status
                    }
                }
            }

            publicSubCommand {
                name = "list"
                description = "Lists all currently whitelisted players."

                action {
                    Logger.log("${user.asUserOrNull()?.username} requested list of whitelisted players!")
                    val whitelistedPlayers = whitelistManager.whitelistedPlayersAsString()
                    val response =
                        if (whitelistedPlayers.isEmpty()) "No players currently whitelisted."
                        else "Players currently whitelisted: $whitelistedPlayers"
                    channel.createMessageWithLifetime(timeout) {
                        content = response
                    }
                }
            }
        }
    }
}
