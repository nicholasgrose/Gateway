package com.rose.gateway.discord.bot.extensions.list

import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.ephemeralSlashCommand
import com.kotlindiscord.kord.extensions.types.editingPaginator
import com.kotlindiscord.kord.extensions.types.respond
import com.rose.gateway.config.PluginConfig
import com.rose.gateway.config.extensions.listExtensionEnabled
import com.rose.gateway.config.extensions.maxPlayersPerPage
import com.rose.gateway.config.extensions.primaryColor
import com.rose.gateway.discord.bot.extensions.ExtensionToggle
import com.rose.gateway.discord.text.discordBoldSafe
import com.rose.gateway.minecraft.logging.Logger
import com.rose.gateway.minecraft.server.ServerInfo
import com.rose.gateway.shared.collections.group
import dev.kord.common.Color
import dev.kord.rest.builder.message.create.embed
import org.koin.core.component.inject

/**
 * A Discord bot extension providing Discord commands for listing players
 *
 * @constructor Create a "list extension"
 */
class ListExtension : Extension() {
    companion object : ExtensionToggle {
        private val config: PluginConfig by inject()

        override fun extensionName(): String = "list"

        override fun extensionConstructor(): () -> Extension = ::ListExtension

        override fun isEnabled(): Boolean = config.listExtensionEnabled()
    }

    override val name: String = extensionName()

    override suspend fun setup() {
        ephemeralSlashCommand {
            name = "list"
            description = "Lists all online players"

            action {
                Logger.info("${user.asUserOrNull()?.username} requested player list!")

                val maxPlayersPerPage = config.maxPlayersPerPage()
                val playerPages = ServerInfo.onlinePlayers.group(maxPlayersPerPage)

                if (playerPages.isEmpty()) {
                    respond {
                        embed {
                            title = "Online Players"
                            description = "No players online."
                            color = Color(config.primaryColor().value())
                        }
                    }

                    return@action
                }

                editingPaginator {
                    for ((pageIndex, subset) in playerPages.withIndex()) {
                        page {
                            title = "Online Players"
                            description = subset.withIndex().joinToString("\n") { (playerIndex, player) ->
                                val playerNumber = (pageIndex * maxPlayersPerPage) + playerIndex + 1

                                "**$playerNumber.** ${player.name.discordBoldSafe()}"
                            }
                            color = Color(config.primaryColor().value())
                        }
                    }
                }.send()
            }
        }
    }
}
