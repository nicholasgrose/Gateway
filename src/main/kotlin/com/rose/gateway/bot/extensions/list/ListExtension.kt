package com.rose.gateway.bot.extensions.list

import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.chatCommand
import com.rose.gateway.GatewayPlugin
import com.rose.gateway.Logger
import com.rose.gateway.bot.extensions.ToggleableExtension
import com.rose.gateway.bot.message.MessageLifetime.respondWithLifetime
import com.rose.gateway.configuration.specs.PluginSpec
import com.rose.gateway.minecraft.server.ServerInfo
import com.rose.gateway.shared.configurations.BotConfiguration.commandTimeout

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
    private val timeout = plugin.configuration.commandTimeout()

    override suspend fun setup() {
        chatCommand {
            name = "list"
            aliases = arrayOf("ls", "l")
            description = "Gives a list of all online players."

            action {
                Logger.log("${user?.asUserOrNull()?.username} requested player list!")
                val playerList = ServerInfo.playerListAsString()
                val response =
                    if (playerList.isEmpty()) "No players online."
                    else "Players online: $playerList"
                message.respondWithLifetime(timeout) {
                    content = response
                }
            }
        }
    }
}
