package xyz.rose.gateway.discord

import xyz.rose.gateway.discord.config.DiscordConfig
import xyz.rose.gateway.platform.Platform
import xyz.rose.gateway.plugin.Plugin
import xyz.rose.gateway.registry.Registrar

/**
 * Discord platform implementation.
 * This platform connects to Discord using the KordeX library.
 *
 * @property config The Discord platform configuration.
 */
class DiscordPlatform(
    val config: DiscordConfig
) : Platform, Registrar<Plugin> {
    /**
     * Returns a list of plugins provided by this platform.
     *
     * @return A list of Discord plugins.
     */
    override fun registrations(): List<Plugin> {
        return listOf(
            KordexMessagePlugin(config),
            KordexAllowlistPlugin(config),
            KordexPerformancePlugin(config),
            KordexAboutPlugin(config),
            KordexCountingPlugin(config),
            KordexConnectPlugin(config)
        )
    }
}

/**
 * Discord message plugin implementation.
 */
class KordexMessagePlugin(
    private val config: DiscordConfig
) : Plugin

/**
 * Discord allowlist plugin implementation.
 */
class KordexAllowlistPlugin(
    private val config: DiscordConfig
) : Plugin

/**
 * Discord performance plugin implementation.
 */
class KordexPerformancePlugin(
    private val config: DiscordConfig
) : Plugin

/**
 * Discord about plugin implementation.
 */
class KordexAboutPlugin(
    private val config: DiscordConfig
) : Plugin

/**
 * Discord counting plugin implementation.
 */
class KordexCountingPlugin(
    private val config: DiscordConfig
) : Plugin

/**
 * Discord connect plugin implementation.
 */
class KordexConnectPlugin(
    private val config: DiscordConfig
) : Plugin
