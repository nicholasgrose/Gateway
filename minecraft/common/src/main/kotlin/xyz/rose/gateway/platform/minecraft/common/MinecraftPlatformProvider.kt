package xyz.rose.gateway.platform.minecraft.common

import org.koin.core.module.Module
import xyz.rose.gateway.core.platform.PlatformProvider

/**
 * The common Minecraft platform provider.
 * This provides default behaviors for Minecraft platforms to hook into,
 * but they must implement the required methods.
 *
 * @constructor Create a new common Minecraft platform provider
 */
class MinecraftPlatformProvider : PlatformProvider {
    override fun createRuntimeModule(): Module {
        TODO("Not yet implemented")
    }
}
