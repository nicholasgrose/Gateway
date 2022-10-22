package com.rose.gateway.discord.bot.extensions.ip

import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.ephemeralSlashCommand
import com.kotlindiscord.kord.extensions.types.respond
import com.rose.gateway.config.PluginConfig
import com.rose.gateway.config.extensions.displayIp
import com.rose.gateway.config.extensions.ipExtensionEnabled
import com.rose.gateway.discord.bot.extensions.ExtensionToggle
import com.rose.gateway.minecraft.logging.Logger
import org.koin.core.component.inject

/**
 * A Discord bot extension providing Discord commands regarding the server IP
 *
 * @constructor Creates an "IP extension"
 */
class IpExtension : Extension() {
    companion object : ExtensionToggle {
        private val config: PluginConfig by inject()

        override fun extensionName(): String = "ip"

        override fun extensionConstructor(): () -> Extension = ::IpExtension

        override fun isEnabled(): Boolean = config.ipExtensionEnabled()
    }

    override val name = extensionName()

    override suspend fun setup() {
        ephemeralSlashCommand {
            name = "ip"
            description = "Displays the server IP."

            action {
                Logger.info("${user.asUserOrNull()?.username} requested server IP!")

                respond {
                    content = "Current IP: ```${config.displayIp()}```"
                }
            }
        }
    }
}
