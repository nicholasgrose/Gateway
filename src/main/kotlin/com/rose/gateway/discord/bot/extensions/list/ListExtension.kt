package com.rose.gateway.discord.bot.extensions.list

import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.ephemeralSlashCommand
import com.kotlindiscord.kord.extensions.types.respond
import com.rose.gateway.config.PluginConfig
import com.rose.gateway.config.extensions.listExtensionEnabled
import com.rose.gateway.discord.bot.extensions.ExtensionToggle
import com.rose.gateway.minecraft.logging.Logger
import com.rose.gateway.minecraft.server.ServerInfo
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
            description = "Lists all online players."

            action {
                Logger.info("${user.asUserOrNull()?.username} requested player list!")

                val playerList = ServerInfo.onlinePlayers
                val response = if (playerList.isEmpty()) "No players online." else {
                    val playerListString = ServerInfo.onlinePlayers.joinToString(", ") { player -> player.name }

                    "Players online: $playerListString"
                }

                respond {
                    content = response
                }
            }
        }
    }
}
