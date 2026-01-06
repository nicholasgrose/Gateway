package xyz.rose.gateway.platform.minecraft.common.config

import kotlinx.serialization.KSerializer
import xyz.rose.gateway.core.config.GatewayConfigSchema
import kotlin.reflect.KClass

/**
 * The config schema for the Minecraft platform
 *
 * @constructor Create a new Minecraft config schema
 */
class MinecraftConfigSchema : GatewayConfigSchema<MinecraftConfig> {
    override val key: String = "minecraft"
    override val serializer: KSerializer<MinecraftConfig> = MinecraftConfig.serializer()
    override val injectableType: KClass<MinecraftConfig> = MinecraftConfig::class
    override val default: MinecraftConfig = MinecraftConfig()
}
