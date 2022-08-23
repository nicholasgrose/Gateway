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

class ListExtension : Extension() {
    companion object : ExtensionToggle {
        val config: PluginConfig by inject()

        override fun extensionName(): String {
            return "list"
        }

        override fun extensionConstructor(): () -> Extension {
            return ::ListExtension
        }

        override fun isEnabled(): Boolean {
            return config.listExtensionEnabled()
        }
    }

    override val name: String = extensionName()

    override suspend fun setup() {
        ephemeralSlashCommand {
            name = "list"
            description = "Gives a list of all online players."

            action {
                Logger.info("${user.asUserOrNull()?.username} requested player list!")
                val playerList = ServerInfo.playerListAsString()
                val response =
                    if (playerList.isEmpty()) "No players online."
                    else "Players online: $playerList"
                respond {
                    content = response
                }
            }
        }
    }
}
