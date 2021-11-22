package com.rose.gateway.bot.extensions.about

import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.ephemeralSlashCommand
import com.kotlindiscord.kord.extensions.types.respond
import com.rose.gateway.GatewayPlugin
import com.rose.gateway.Logger
import com.rose.gateway.bot.extensions.ToggleableExtension
import com.rose.gateway.configuration.specs.PluginSpec

class AboutExtension : Extension() {
    companion object : ToggleableExtension {
        override fun extensionName(): String {
            return "about"
        }

        override fun extensionConstructor(): () -> Extension {
            return ::AboutExtension
        }

        override fun isEnabled(plugin: GatewayPlugin): Boolean {
            val enabledSpec = PluginSpec.BotSpec.ExtensionsSpec.AboutSpec.enabled
            return plugin.configuration[enabledSpec]
        }
    }

    override val name = extensionName()
    val plugin = bot.getKoin().get<GatewayPlugin>()

    override suspend fun setup() {
        ephemeralSlashCommand {
            name = "version"
            description = "Gives the current version of the Gateway plugin."

            action {
                Logger.logInfo("${user.asUserOrNull()?.username} requested plugin version!")
                respond {
                    content =
                        "I am currently version ${GatewayPlugin.VERSION}. All versions are available at https://github.com/nicholasgrose/Gateway/."
                }
            }
        }

        ephemeralSlashCommand {
            name = "blockgod"
            description = "Summon the block god for a moment."

            action {
                Logger.logInfo("${user.asUserOrNull()?.username} used the super secret command!")
                respond {
                    @Suppress("HttpUrlsUsage")
                    content = "http://www.scpwiki.com/church-of-the-broken-god-hub"
                }
            }
        }
    }
}
