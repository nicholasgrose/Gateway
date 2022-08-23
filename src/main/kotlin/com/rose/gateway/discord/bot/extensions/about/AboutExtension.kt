package com.rose.gateway.discord.bot.extensions.about

import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.ephemeralSlashCommand
import com.kotlindiscord.kord.extensions.types.respond
import com.rose.gateway.GatewayPlugin
import com.rose.gateway.config.PluginConfig
import com.rose.gateway.config.extensions.aboutExtensionEnabled
import com.rose.gateway.discord.bot.extensions.ExtensionToggle
import com.rose.gateway.minecraft.logging.Logger
import org.koin.core.component.inject

class AboutExtension : Extension() {
    companion object : ExtensionToggle {
        val config: PluginConfig by inject()

        override fun extensionName(): String {
            return "about"
        }

        override fun extensionConstructor(): () -> Extension {
            return ::AboutExtension
        }

        override fun isEnabled(): Boolean {
            return config.aboutExtensionEnabled()
        }
    }

    val plugin: GatewayPlugin by inject()
    override val name = extensionName()

    override suspend fun setup() {
        ephemeralSlashCommand {
            name = "version"
            description = "Gives the current version of the Gateway plugin."

            action {
                Logger.info("${user.asUserOrNull()?.username} requested plugin version!")
                respond {
                    content = "I am currently version ${plugin.description.version}." +
                        "All versions are available at https://github.com/nicholasgrose/Gateway/."
                }
            }
        }

        ephemeralSlashCommand {
            name = "blockgod"
            description = "Summon the block god for a moment."

            action {
                Logger.info("${user.asUserOrNull()?.username} used the super secret command!")
                respond {
                    @Suppress("HttpUrlsUsage")
                    content = "http://www.scpwiki.com/church-of-the-broken-god-hub"
                }
            }
        }
    }
}
