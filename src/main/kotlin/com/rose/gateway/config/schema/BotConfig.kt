package com.rose.gateway.config.schema

import com.rose.gateway.config.markers.ConfigItem
import com.rose.gateway.config.markers.ConfigObject
import com.rose.gateway.discord.bot.DiscordBot
import com.rose.gateway.shared.concurrency.PluginCoroutineScope
import com.rose.gateway.shared.serialization.SurrogateBasedSerializer
import com.rose.gateway.shared.serialization.SurrogateConverter
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * The config for the Discord bot
 *
 * @property token The token for the Discord bot
 * @property botChannels The channels in which the Discord bot should operate
 * @property extensions The config for the Discord bot's extensions
 * @constructor Creates a bot config with the given data
 *
 * @param token The token to use initially
 * @param botChannels The bot channels to use initially
 */
@Serializable(with = BotConfigSerializer::class)
class BotConfig(
    token: String,
    botChannels: List<String>,
    @ConfigItem val extensions: ExtensionsConfig,
) : KoinComponent, ConfigObject {
    private val pluginCoroutineScope: PluginCoroutineScope by inject()
    private val bot: DiscordBot by inject()

    /**
     * The Discord bot's bot token
     */
    @ConfigItem(
        "The token used by the bot to access discord. Accessible at https://discord.com/developers/applications/.",
    )
    var token = token
        set(value) {
            field = value
            pluginCoroutineScope.launch {
                bot.rebuild()
            }
        }

    /**
     * The names of the DiscordChannels the bot will read and post to
     */
    @ConfigItem("The names of the channels in which the bot should respond to commands and post/accept chat messages.")
    var botChannels = botChannels
        set(value) {
            field = value
            runBlocking { bot.context.fillBotChannels() }
        }
}

/**
 * Surrogate for serialization of [BotConfig]
 *
 * @property token The token for the Discord bot
 * @property botChannels The channels in which the Discord bot should operate
 * @property extensions The config for the Discord bot's extensions
 * @constructor Create a bot config surrogate
 *
 * @see BotConfig
 * @see BotConfigSerializer
 */
@Serializable
data class BotConfigSurrogate(
    val token: String,
    val botChannels: List<String>,
    val extensions: ExtensionsConfig,
) {
    companion object : SurrogateConverter<BotConfig, BotConfigSurrogate> {
        override fun fromBase(base: BotConfig): BotConfigSurrogate = BotConfigSurrogate(
            base.token,
            base.botChannels,
            base.extensions,
        )

        override fun toBase(surrogate: BotConfigSurrogate): BotConfig = BotConfig(
            surrogate.token,
            surrogate.botChannels,
            surrogate.extensions,
        )
    }
}

/**
 * The serializer for [BotConfig]
 *
 * @see BotConfig
 * @see BotConfigSurrogate
 */
object BotConfigSerializer :
    SurrogateBasedSerializer<BotConfig, BotConfigSurrogate>(BotConfigSurrogate.serializer(), BotConfigSurrogate)
