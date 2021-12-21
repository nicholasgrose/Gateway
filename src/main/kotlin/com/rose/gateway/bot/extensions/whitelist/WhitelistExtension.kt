package com.rose.gateway.bot.extensions.whitelist

import com.kotlindiscord.kord.extensions.commands.application.slash.ephemeralSubCommand
import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.ephemeralSlashCommand
import com.kotlindiscord.kord.extensions.types.respond
import com.rose.gateway.GatewayPlugin
import com.rose.gateway.Logger
import com.rose.gateway.bot.extensions.ToggleableExtension
import com.rose.gateway.minecraft.whitelist.Whitelist
import com.rose.gateway.minecraft.whitelist.WhitelistState
import com.rose.gateway.shared.configurations.BotConfiguration.whitelistExtensionEnabled

class WhitelistExtension : Extension() {
    companion object : ToggleableExtension {
        override fun extensionName(): String {
            return "whitelist"
        }

        override fun extensionConstructor(): () -> Extension {
            return ::WhitelistExtension
        }

        override fun isEnabled(plugin: GatewayPlugin): Boolean {
            return plugin.configuration.whitelistExtensionEnabled()
        }
    }

    override val name: String = extensionName()
    val plugin = bot.getKoin().get<GatewayPlugin>()
    private val whitelistManager = Whitelist(plugin)

    override suspend fun setup() {
        ephemeralSlashCommand {
            name = "whitelist"
            description = "Runs an operation that relates to the server whitelist."

            ephemeralSubCommand(::WhitelistArguments) {
                name = "add"
                description = "Adds a player to the whitelist."

                action {
                    Logger.logInfo("${user.asUserOrNull()?.username} added ${arguments.username} to whitelist!")
                    val status = when (whitelistManager.addToWhitelist(arguments.username)) {
                        WhitelistState.STATE_MODIFIED -> "${arguments.username} successfully added to whitelist."
                        WhitelistState.STATE_SUSTAINED -> "${arguments.username} already exists in whitelist."
                        WhitelistState.STATE_INVALID -> "An error occurred adding ${arguments.username} to whitelist."
                    }
                    respond {
                        content = status
                    }
                }
            }

            ephemeralSubCommand(::WhitelistArguments) {
                name = "remove"
                description = "Removes a player from the whitelist."

                action {
                    Logger.logInfo("${user.asUserOrNull()?.username} removed ${arguments.username} from whitelist!")
                    val status = when (whitelistManager.removeFromWhitelist(arguments.username)) {
                        WhitelistState.STATE_MODIFIED -> "${arguments.username} successfully removed from whitelist."
                        WhitelistState.STATE_SUSTAINED -> "${arguments.username} does not exist in whitelist."
                        WhitelistState.STATE_INVALID -> "An error occurred removing ${arguments.username} from whitelist."
                    }
                    respond {
                        content = status
                    }
                }
            }

            ephemeralSubCommand {
                name = "list"
                description = "Lists all currently whitelisted players."

                action {
                    Logger.logInfo("${user.asUserOrNull()?.username} requested list of whitelisted players!")
                    val whitelistedPlayers = whitelistManager.whitelistedPlayersAsString()
                    val response =
                        if (whitelistedPlayers.isEmpty()) "No players currently whitelisted."
                        else "Players currently whitelisted: $whitelistedPlayers"
                    respond {
                        content = response
                    }
                }
            }
        }
    }
}
