package com.rose.gateway.config.schema

import com.rose.gateway.GatewayPlugin
import com.rose.gateway.config.markers.ConfigItem
import com.rose.gateway.config.markers.ConfigObject
import com.rose.gateway.config.markers.SurrogateBasedSerializer
import com.rose.gateway.config.markers.SurrogateConverter
import com.rose.gateway.discord.bot.DiscordBot
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@Serializable(with = BotConfigSerializer::class)
class BotConfig(
    token: String,
    botChannels: List<String>,
    @ConfigItem
    val extensions: ExtensionsConfig
) : KoinComponent, ConfigObject {
    private val plugin: GatewayPlugin by inject()
    private val bot: DiscordBot by inject()

    @ConfigItem(
        """
            The token used by the bot to access discord.
            Accessible at https://discord.com/developers/applications/.
        """
    )
    var token = token
        set(value) {
            field = value
            plugin.launch {
                bot.rebuild()
            }
        }

    @ConfigItem("The names of the channels in which the bot should respond to commands and post/accept chat messages.")
    var botChannels = botChannels
        set(value) {
            field = value
            runBlocking { bot.fillBotChannels() }
        }
}

@Serializable
data class BotConfigSurrogate(
    val token: String,
    val botChannels: List<String>,
    val extensions: ExtensionsConfig
) {
    companion object : SurrogateConverter<BotConfig, BotConfigSurrogate> {
        override fun fromBase(base: BotConfig): BotConfigSurrogate = BotConfigSurrogate(
            base.token,
            base.botChannels,
            base.extensions
        )

        override fun toBase(surrogate: BotConfigSurrogate): BotConfig = BotConfig(
            surrogate.token,
            surrogate.botChannels,
            surrogate.extensions
        )
    }
}

object BotConfigSerializer :
    SurrogateBasedSerializer<BotConfig, BotConfigSurrogate>(BotConfigSurrogate.serializer(), BotConfigSurrogate)
