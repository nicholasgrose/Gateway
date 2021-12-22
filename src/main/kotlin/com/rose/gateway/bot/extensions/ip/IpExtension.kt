package com.rose.gateway.bot.extensions.ip

import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.ephemeralSlashCommand
import com.kotlindiscord.kord.extensions.types.respond
import com.rose.gateway.GatewayPlugin
import com.rose.gateway.bot.extensions.ToggleableExtension
import com.rose.gateway.shared.configurations.BotConfiguration.displayIp
import com.rose.gateway.shared.configurations.BotConfiguration.ipExtensionEnabled

class IpExtension : Extension() {
    companion object : ToggleableExtension {
        override fun extensionName(): String {
            return "ip"
        }

        override fun extensionConstructor(): () -> Extension {
            return ::IpExtension
        }

        override fun isEnabled(plugin: GatewayPlugin): Boolean {
            return plugin.configuration.ipExtensionEnabled()
        }
    }

    override val name = extensionName()
    val plugin = bot.getKoin().get<GatewayPlugin>()

    override suspend fun setup() {
        ephemeralSlashCommand {
            name = "ip"
            description = "Displays the configured server display IP."

            action {
                respond {
                    content = "Current IP: ```${plugin.configuration.displayIp()}```"
                }
            }
        }
    }
}
