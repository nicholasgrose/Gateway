package com.rose.gateway.bot.extensions.list

import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.ephemeralSlashCommand
import com.kotlindiscord.kord.extensions.types.respond
import com.rose.gateway.GatewayPlugin
import com.rose.gateway.Logger
import com.rose.gateway.bot.extensions.ToggleableExtension
import com.rose.gateway.configuration.specs.PluginSpec
import com.rose.gateway.minecraft.server.ServerInfo

class ListExtension : Extension() {
    companion object : ToggleableExtension {
        override fun extensionName(): String {
            return "list"
        }

        override fun extensionConstructor(): () -> Extension {
            return ::ListExtension
        }

        override fun isEnabled(plugin: GatewayPlugin): Boolean {
            val enabledSpec = PluginSpec.BotSpec.ExtensionsSpec.ListSpec.enabled
            return plugin.configuration[enabledSpec]
        }
    }

    override val name: String = extensionName()
    val plugin = getKoin().get<GatewayPlugin>()

    override suspend fun setup() {
        ephemeralSlashCommand {
            name = "list"
            description = "Gives a list of all online players."

            action {
                Logger.logInfo("${user.asUserOrNull()?.username} requested player list!")
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
