package com.rose.gateway.discord.bot.extensions.ip

import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.ephemeralSlashCommand
import com.kotlindiscord.kord.extensions.types.respond
import com.rose.gateway.GatewayPlugin
import com.rose.gateway.config.PluginConfig
import com.rose.gateway.config.extensions.displayIp
import com.rose.gateway.config.extensions.ipExtensionEnabled
import com.rose.gateway.discord.bot.extensions.ToggleableExtension
import org.koin.core.component.inject

class IpExtension : Extension() {
    companion object : ToggleableExtension {
        val config: PluginConfig by inject()

        override fun extensionName(): String {
            return "ip"
        }

        override fun extensionConstructor(): () -> Extension {
            return ::IpExtension
        }

        override fun isEnabled(plugin: GatewayPlugin): Boolean {
            return config.ipExtensionEnabled()
        }
    }

    override val name = extensionName()

    override suspend fun setup() {
        ephemeralSlashCommand {
            name = "ip"
            description = "Displays the configured server display IP."

            action {
                respond {
                    content = "Current IP: ```${config.displayIp()}```"
                }
            }
        }
    }
}
