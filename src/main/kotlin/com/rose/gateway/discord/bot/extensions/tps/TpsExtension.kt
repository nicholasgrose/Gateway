package com.rose.gateway.discord.bot.extensions.tps

import com.rose.gateway.config.PluginConfig
import com.rose.gateway.config.access.secondaryColor
import com.rose.gateway.config.access.tpsExtensionEnabled
import com.rose.gateway.discord.bot.extensions.ExtensionToggle
import com.rose.gateway.minecraft.server.ServerInfo
import dev.kord.common.Color
import dev.kord.rest.builder.message.embed
import dev.kordex.core.extensions.Extension
import dev.kordex.core.extensions.ephemeralSlashCommand
import gateway.i18n.Translations
import org.koin.core.component.inject
import kotlin.math.roundToInt

/**
 * Extension that provides ways to query server TPS data
 *
 * @constructor Create a TPS extension
 */
class TpsExtension : Extension() {
    /**
     * Toggle for the TPS extension
     *
     * @constructor Creates the extension's toggle
     */
    companion object : ExtensionToggle {
        private val config: PluginConfig by inject()

        override fun extensionName(): String = "tps"

        override fun extensionConstructor(): () -> Extension = ::TpsExtension

        override fun isEnabled(): Boolean = config.tpsExtensionEnabled()
    }

    override val name: String = extensionName()

    override suspend fun setup() {
        ephemeralSlashCommand {
            name = Translations.Commands.Tps.name
            description = Translations.Commands.Tps.description

            action {
                val tps = ServerInfo.tps

                respond {
                    embed {
                        title = "TPS (ticks/sec)"
                        description =
                            """
                            **1m:** ${tps.oneMinute.roundToInt()} t/s
                            **5m:** ${tps.fiveMinute.roundToInt()} t/s
                            **15m:** ${tps.fifteenMinute.roundToInt()} t/s
                            """.trimIndent()
                        color = Color(config.secondaryColor().value())
                    }
                }
            }
        }
    }
}
