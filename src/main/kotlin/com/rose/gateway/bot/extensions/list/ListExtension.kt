package com.rose.gateway.bot.extensions.list

import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.ephemeralSlashCommand
import com.kotlindiscord.kord.extensions.types.respond
import com.rose.gateway.GatewayPlugin
import com.rose.gateway.Logger
import com.rose.gateway.bot.extensions.ToggleableExtension
import com.rose.gateway.configuration.PluginConfiguration
import com.rose.gateway.minecraft.server.ServerInfo
import com.rose.gateway.shared.configurations.listExtensionEnabled
import org.koin.core.component.inject

class ListExtension : Extension() {
    companion object : ToggleableExtension {
        val config: PluginConfiguration by inject()

        override fun extensionName(): String {
            return "list"
        }

        override fun extensionConstructor(): () -> Extension {
            return ::ListExtension
        }

        override fun isEnabled(plugin: GatewayPlugin): Boolean {
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
