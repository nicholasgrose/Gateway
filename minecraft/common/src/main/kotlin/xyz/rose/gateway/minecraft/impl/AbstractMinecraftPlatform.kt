package xyz.rose.gateway.minecraft.impl

import xyz.rose.gateway.minecraft.MinecraftPlatform
import xyz.rose.gateway.minecraft.config.MinecraftConfig
import xyz.rose.gateway.minecraft.plugin.MinecraftChatPlugin
import xyz.rose.gateway.minecraft.plugin.MinecraftPlayerCountPlugin
import xyz.rose.gateway.minecraft.plugin.MinecraftTpsPlugin
import xyz.rose.gateway.minecraft.plugin.MinecraftVersionInfoPlugin
import xyz.rose.gateway.minecraft.plugin.MinecraftWhitelistPlugin
import xyz.rose.gateway.minecraft.plugin.impl.MinecraftChatPluginImpl
import xyz.rose.gateway.minecraft.plugin.impl.MinecraftPlayerCountPluginImpl
import xyz.rose.gateway.minecraft.plugin.impl.MinecraftTpsPluginImpl
import xyz.rose.gateway.minecraft.plugin.impl.MinecraftVersionInfoPluginImpl
import xyz.rose.gateway.minecraft.plugin.impl.MinecraftWhitelistPluginImpl
import xyz.rose.gateway.plugin.Plugin

/**
 * Abstract implementation of the MinecraftPlatform interface.
 * This class provides common functionality for all Minecraft platforms.
 *
 * @property config The Minecraft platform configuration.
 */
abstract class AbstractMinecraftPlatform(
    override val config: MinecraftConfig
) : MinecraftPlatform {
    protected val whitelistPlugin: MinecraftWhitelistPlugin = MinecraftWhitelistPluginImpl()
    protected val tpsPlugin: MinecraftTpsPlugin = MinecraftTpsPluginImpl()
    protected val versionInfoPlugin: MinecraftVersionInfoPlugin = MinecraftVersionInfoPluginImpl()
    protected val playerCountPlugin: MinecraftPlayerCountPlugin = MinecraftPlayerCountPluginImpl()
    protected val chatPlugin: MinecraftChatPlugin = MinecraftChatPluginImpl()

    /**
     * Returns a list of plugins to be registered.
     *
     * @return A list of plugins to be registered.
     */
    override fun registrations(): List<Plugin> {
        return listOf(
            whitelistPlugin,
            tpsPlugin,
            versionInfoPlugin,
            playerCountPlugin,
            chatPlugin
        )
    }

    /**
     * Loads the platform configuration.
     *
     * @return The platform configuration.
     */
    override fun loadConfig(): MinecraftConfig {
        return config
    }
}
