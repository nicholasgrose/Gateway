package xyz.rose.gateway.platform.minecraft.common

import org.koin.core.module.Module
import xyz.rose.gateway.core.config.GatewayConfig
import xyz.rose.gateway.core.config.GatewayConfigSchema
import xyz.rose.gateway.core.platform.GatewayPlatformProvider

/**
 * The common Minecraft platform provider.
 * This provides default behaviors for Minecraft platforms to hook into,
 * but they must implement the required methods.
 *
 * @constructor Create a new common Minecraft platform provider
 */
class MinecraftPlatformProvider : GatewayPlatformProvider {
    override fun getConfigSchema(): GatewayConfigSchema<*> {
        TODO("Not yet implemented")
    }

    override fun createRuntimeModule(config: GatewayConfig): Module {
        TODO("Not yet implemented")
    }
}
