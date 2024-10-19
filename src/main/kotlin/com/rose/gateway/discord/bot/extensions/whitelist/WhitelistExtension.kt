package com.rose.gateway.discord.bot.extensions.whitelist

import com.kotlindiscord.kord.extensions.commands.application.slash.EphemeralSlashCommandContext
import com.kotlindiscord.kord.extensions.commands.application.slash.ephemeralSubCommand
import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.ephemeralSlashCommand
import com.rose.gateway.config.PluginConfig
import com.rose.gateway.config.access.maxPlayersPerWhitelistPage
import com.rose.gateway.config.access.primaryColor
import com.rose.gateway.config.access.secondaryColor
import com.rose.gateway.config.access.warningColor
import com.rose.gateway.config.access.whitelistExtensionEnabled
import com.rose.gateway.discord.bot.extensions.ExtensionToggle
import com.rose.gateway.discord.bot.message.groupAndPaginateItems
import com.rose.gateway.discord.text.discordBoldSafe
import com.rose.gateway.minecraft.logging.Logger
import com.rose.gateway.minecraft.whitelist.Whitelist
import com.rose.gateway.minecraft.whitelist.WhitelistState
import com.rose.gateway.shared.text.plurality
import dev.kord.common.Color
import dev.kord.rest.builder.message.embed
import org.koin.core.component.inject

/**
 * A Discord bot extension providing Discord commands to modify the whitelist
 *
 * @constructor Create a "whitelist extension"
 */
class WhitelistExtension : Extension() {
    /**
     * Companion
     *
     * @constructor Create empty Companion
     */
    companion object : ExtensionToggle {
        private val config: PluginConfig by inject()

        override fun extensionName(): String = "whitelist"

        override fun extensionConstructor(): () -> Extension = ::WhitelistExtension

        override fun isEnabled(): Boolean = config.whitelistExtensionEnabled()
    }

    override val name: String = extensionName()

    override suspend fun setup() {
        ephemeralSlashCommand {
            name = "whitelist"
            description = "Runs an operation that relates to the server whitelist."

            ephemeralSubCommand(::WhitelistArguments) {
                name = "add"
                description = "Adds a player to the whitelist"

                action {
                    Logger.info("${user.asUserOrNull()?.username} added ${arguments.username} to whitelist!")

                    val state = Whitelist.addPlayer(arguments.username)

                    this.whitelistAddResponse(state)
                }
            }

            ephemeralSubCommand(::WhitelistArguments) {
                name = "remove"
                description = "Removes a player from the whitelist"

                action {
                    Logger.info("${user.asUserOrNull()?.username} removed ${arguments.username} from whitelist!")

                    val state = Whitelist.removePlayer(arguments.username)

                    whitelistRemoveResponse(state)
                }
            }

            ephemeralSubCommand {
                name = "list"
                description = "Lists all currently whitelisted players"

                action {
                    Logger.info("${user.asUserOrNull()?.username} requested list of whitelisted players!")

                    val maxPlayersPerPage = config.maxPlayersPerWhitelistPage()
                    val whitelistedPlayers = Whitelist.players
                    val whitelistedPlayerCount = whitelistedPlayers.size

                    groupAndPaginateItems(whitelistedPlayers, maxPlayersPerPage, {
                        embed {
                            title = "0 Whitelisted Players"
                            description = "No players in whitelist."
                            color = Color(config.secondaryColor().value())
                        }
                    }, { groupIndex, group ->
                        page {
                            title = plurality(
                                whitelistedPlayerCount,
                                "1 Whitelisted Player",
                                "$whitelistedPlayerCount Whitelisted Players",
                            )
                            description = group.withIndex().joinToString("\n") { (playerIndex, player) ->
                                val playerNumber = (groupIndex * maxPlayersPerPage) + playerIndex + 1
                                val playerName = player.name?.discordBoldSafe() ?: player.uniqueId

                                "**$playerNumber.** $playerName"
                            }
                            color = Color(config.secondaryColor().value())
                        }
                    })
                }
            }
        }
    }

    private suspend fun EphemeralSlashCommandContext<WhitelistArguments, *>.whitelistAddResponse(
        state: WhitelistState,
    ) {
        respond {
            when (state) {
                WhitelistState.STATE_MODIFIED -> embed {
                    title = "Whitelist Changed"
                    description = "**${arguments.username.discordBoldSafe()}** successfully added to whitelist."
                    color = Color(WhitelistExtension.config.primaryColor().value())
                }

                WhitelistState.STATE_SUSTAINED -> embed {
                    title = "Whitelist Not Changed"
                    description = "**${arguments.username.discordBoldSafe()}** already exists in whitelist."
                    color = Color(WhitelistExtension.config.secondaryColor().value())
                }

                WhitelistState.STATE_INVALID -> embed {
                    title = "Whitelist Addition Failed"
                    description = "An error occurred adding **${arguments.username.discordBoldSafe()}** to whitelist."
                    color = Color(WhitelistExtension.config.warningColor().value())
                }
            }
        }
    }

    private suspend fun EphemeralSlashCommandContext<WhitelistArguments, *>.whitelistRemoveResponse(
        state: WhitelistState,
    ) {
        respond {
            when (state) {
                WhitelistState.STATE_MODIFIED -> embed {
                    title = "Whitelist Changed"
                    description = "**${arguments.username.discordBoldSafe()}** successfully removed from whitelist."
                    color = Color(WhitelistExtension.config.primaryColor().value())
                }

                WhitelistState.STATE_SUSTAINED -> embed {
                    title = "Whitelist Not Changed"
                    description = "**${arguments.username.discordBoldSafe()}** does not exist in whitelist."
                    color = Color(WhitelistExtension.config.secondaryColor().value())
                }

                WhitelistState.STATE_INVALID -> embed {
                    title = "Whitelist Removal Failed"
                    description =
                        "Error occurred removing" + " **${arguments.username.discordBoldSafe()}** from whitelist."
                    color = Color(WhitelistExtension.config.warningColor().value())
                }
            }
        }
    }
}
