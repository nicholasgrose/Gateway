package com.rose.gateway.bot.extensions.whitelist

import com.kotlindiscord.kord.extensions.extensions.Extension
import com.rose.gateway.Logger
import com.rose.gateway.bot.message.MessageLifetime.respondWithLifetime
import com.rose.gateway.minecraft.whitelist.Whitelist
import com.rose.gateway.minecraft.whitelist.WhitelistState

class WhitelistExtension : Extension() {
    override val name: String = "whitelist"

    override suspend fun setup() {
        group {
            name = "whitelist"
            aliases = arrayOf("wl", "w")
            description = "Runs an operation that relates to the server whitelist."

            command(::WhitelistArguments) {
                name = "add"
                aliases = arrayOf("a", "+")
                description = "Adds a player to the whitelist."

                action {
                    Logger.log("${user?.username} added ${arguments.username} to whitelist!")
                    val status = when (Whitelist.addToWhitelist(arguments.username)) {
                        WhitelistState.STATE_MODIFIED -> "${arguments.username} successfully added to whitelist."
                        WhitelistState.STATE_SUSTAINED -> "${arguments.username} already exists in whitelist."
                        WhitelistState.STATE_INVALID -> "An error occurred adding ${arguments.username} to whitelist."
                    }
                    message.respondWithLifetime {
                        content = status
                    }
                }
            }

            command(::WhitelistArguments) {
                name = "remove"
                aliases = arrayOf("rm", "r", "d", "-")
                description = "Removes a player from the whitelist."

                action {
                    Logger.log("${user?.username} removed ${arguments.username} from whitelist!")
                    val status = when (Whitelist.removeFromWhitelist(arguments.username)) {
                        WhitelistState.STATE_MODIFIED -> "${arguments.username} successfully removed from whitelist."
                        WhitelistState.STATE_SUSTAINED -> "${arguments.username} does not exist in whitelist."
                        WhitelistState.STATE_INVALID -> "An error occurred removing ${arguments.username} from whitelist."
                    }
                    message.respondWithLifetime {
                        content = status
                    }
                }
            }

            command {
                name = "list"
                aliases = arrayOf("ls", "l")
                description = "Lists all currently whitelisted players."

                action {
                    Logger.log("${user?.username} requested list of whitelisted players!")
                    val whitelistedPlayers = Whitelist.listWhitelistedPlayers()
                    val response =
                        if (whitelistedPlayers.isEmpty()) "No players currently whitelisted."
                        else "Players currently whitelisted: $whitelistedPlayers"
                    message.respondWithLifetime {
                        content = response
                    }
                }
            }
        }
    }
}
