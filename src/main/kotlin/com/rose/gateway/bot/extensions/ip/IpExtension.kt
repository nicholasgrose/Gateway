package com.rose.gateway.bot.extensions.ip

import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.ephemeralSlashCommand
import com.kotlindiscord.kord.extensions.types.respond
import com.rose.gateway.GatewayPlugin
import com.rose.gateway.bot.extensions.ToggleableExtension
import com.rose.gateway.configuration.PluginConfiguration
import com.rose.gateway.shared.configurations.displayIp
import com.rose.gateway.shared.configurations.ipExtensionEnabled
import org.koin.core.component.inject

class IpExtension : Extension() {
    companion object : ToggleableExtension {
        val config: PluginConfiguration by inject()

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
