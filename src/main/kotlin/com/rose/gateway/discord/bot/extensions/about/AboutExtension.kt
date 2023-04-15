package com.rose.gateway.discord.bot.extensions.about

import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.ephemeralSlashCommand
import com.kotlindiscord.kord.extensions.types.respond
import com.rose.gateway.config.PluginConfig
import com.rose.gateway.config.access.aboutExtensionEnabled
import com.rose.gateway.config.access.secondaryColor
import com.rose.gateway.discord.bot.extensions.ExtensionToggle
import com.rose.gateway.minecraft.logging.Logger
import dev.kord.common.Color
import dev.kord.rest.builder.message.create.embed
import org.bukkit.Bukkit
import org.koin.core.component.inject

/**
 * A Discord bot extension that provides Discord commands regarding the plugin
 *
 * @constructor Create an "about extension"
 */
class AboutExtension : Extension() {
    companion object : ExtensionToggle {
        private val config: PluginConfig by inject()

        override fun extensionName(): String = "about"

        override fun extensionConstructor(): () -> Extension = ::AboutExtension

        override fun isEnabled(): Boolean = config.aboutExtensionEnabled()
    }

    override val name = extensionName()

    override suspend fun setup() {
        ephemeralSlashCommand {
            name = "version"
            description = "Gives the current version of the Minecraft server"

            action {
                Logger.info("${user.asUserOrNull()?.username} requested plugin version!")

                respond {
                    embed {
                        title = "Minecraft Version"
                        description = Bukkit.getMinecraftVersion()
                        color = Color(config.secondaryColor().value())
                    }
                }
            }
        }

        ephemeralSlashCommand {
            name = "blockgod"
            description = "Summon the block god for but a moment"

            action {
                Logger.info("${user.asUserOrNull()?.username} used the super secret command!")

                respond {
                    embed {
                        title = "Witness The Glory"
                        description = "The Block God must be experienced."
                        url = "https://scp-wiki.wikidot.com/church-of-the-broken-god-hub"
                        color = Color(config.secondaryColor().value())
                    }
                }
            }
        }
    }
}
