package xyz.rose.gateway.platform.minecraft.common.config

import kotlinx.serialization.KSerializer
import xyz.rose.gateway.core.config.ConfigSchema
import kotlin.reflect.KClass

/**
 * The config schema for the Minecraft platform
 *
 * @constructor Create a new Minecraft config schema
 */
class MinecraftConfigSchema : ConfigSchema<MinecraftConfig> {
    override val key: String = "minecraft"
    override val serializer: KSerializer<MinecraftConfig> = MinecraftConfig.serializer()
    override val injectableType: KClass<MinecraftConfig> = MinecraftConfig::class
    override val default: MinecraftConfig = MinecraftConfig(
        address = ServerAddress("localhost", DEFAULT_MINECRAFT_PORT),
        chat = ChatColors(
            primaryColor = "#56EE5C",
            secondaryColor = "#7289DA",
            tertiaryColor = "#F526ED",
            warningColor = "#EB4325"
        )
    )

    companion object {
        const val DEFAULT_MINECRAFT_PORT = 25565
    }
}
