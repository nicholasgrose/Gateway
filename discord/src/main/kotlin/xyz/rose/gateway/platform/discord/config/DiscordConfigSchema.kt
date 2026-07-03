package xyz.rose.gateway.platform.discord.config

import kotlinx.serialization.KSerializer
import xyz.rose.gateway.core.config.ConfigSchema
import kotlin.reflect.KClass

/**
 * The schema for configuring the Discord platform.
 *
 * @constructor Create a new Discord config schema
 */
class DiscordConfigSchema : ConfigSchema<DiscordConfig> {
    override val key: String = "discord"
    override val serializer: KSerializer<DiscordConfig> = DiscordConfig.serializer()
    override val injectableType: KClass<DiscordConfig> = DiscordConfig::class
    override val default: DiscordConfig = DiscordConfig("")
}
