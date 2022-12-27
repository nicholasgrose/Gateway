package com.rose.gateway.discord.bot.extensions.list

import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.ephemeralSlashCommand
import com.rose.gateway.config.PluginConfig
import com.rose.gateway.config.access.listExtensionEnabled
import com.rose.gateway.config.access.maxPlayersPerListPage
import com.rose.gateway.config.access.secondaryColor
import com.rose.gateway.discord.bot.extensions.ExtensionToggle
import com.rose.gateway.discord.bot.message.groupAndPaginateItems
import com.rose.gateway.discord.text.discordBoldSafe
import com.rose.gateway.minecraft.logging.Logger
import com.rose.gateway.minecraft.server.ServerInfo
import com.rose.gateway.shared.text.plurality
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

                val maxPlayersPerPage = config.maxPlayersPerListPage()
                val onlinePlayers = ServerInfo.onlinePlayers
                val onlinePlayerCount = onlinePlayers.size

                groupAndPaginateItems(onlinePlayers, maxPlayersPerPage, {
                    embed {
                        title = "0 Online Players"
                        description = "No players online."
                        color = Color(config.secondaryColor().value())
                    }
                }, { groupIndex, group ->
                    page {
                        title = plurality(
                            onlinePlayerCount,
                            "1 Online Player",
                            "$onlinePlayerCount Player Online"
                        )
                        description = group.withIndex().joinToString("\n") { (playerIndex, player) ->
                            val playerNumber = (groupIndex * maxPlayersPerPage) + playerIndex + 1

                            "**$playerNumber.** ${player.name.discordBoldSafe()}"
                        }
                        color = Color(config.secondaryColor().value())
                    }
                })
            }
        }
    }
}
