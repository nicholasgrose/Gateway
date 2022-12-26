package com.rose.gateway.discord.bot.extensions.tps

import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.ephemeralSlashCommand
import com.kotlindiscord.kord.extensions.types.respond
import com.rose.gateway.config.PluginConfig
import com.rose.gateway.config.extensions.tpsExtensionEnabled
import com.rose.gateway.discord.bot.extensions.ExtensionToggle
import com.rose.gateway.minecraft.server.ServerInfo
import org.koin.core.component.inject
import kotlin.math.roundToInt

/**
 * Extension that provides ways to query server TPS data
 *
 * @constructor Create a TPS extension
 */
class TpsExtension : Extension() {
    companion object : ExtensionToggle {
        private val config: PluginConfig by inject()

        override fun extensionName(): String = "tps"

        override fun extensionConstructor(): () -> Extension = ::TpsExtension

        override fun isEnabled(): Boolean = config.tpsExtensionEnabled()
    }

    override val name: String = extensionName()

    override suspend fun setup() {
        ephemeralSlashCommand {
            name = "tps"
            description = "Queries the server for its current TPS."

            action {
                val tps = ServerInfo.tps

                respond {
                    content = """
                        TPS (ticks/sec):
                        **1m:** ${tps.oneMinute.roundToInt()} t/s
                        **5m:** ${tps.fiveMinute.roundToInt()} t/s
                        **15m:** ${tps.fifteenMinute.roundToInt()} t/s
                    """.trimIndent()
                }
            }
        }
    }
}
