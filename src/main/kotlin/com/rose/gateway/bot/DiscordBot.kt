package com.rose.gateway.bot

import com.kotlindiscord.kord.extensions.DISCORD_GREEN
import com.kotlindiscord.kord.extensions.ExtensibleBot
import com.rose.gateway.Logger
import com.rose.gateway.bot.checks.DefaultCheck
import com.rose.gateway.bot.client.ClientInfo
import com.rose.gateway.bot.status.DynamicStatus
import com.rose.gateway.configuration.Configurator
import com.rose.gateway.configuration.PluginSpec
import dev.kord.core.Kord
import dev.kord.core.entity.Guild
import dev.kord.core.entity.channel.TextChannel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.datetime.Clock

class DiscordBot {
    companion object {
        val bot by lazy {
            runBlocking {
                createBot(Configurator.config[PluginSpec.botToken])
            }
        }

        val kordClient by lazy {
            bot.getKoin().get<Kord>()
        }

        private suspend fun createBot(token: String): ExtensibleBot {
            return ExtensibleBot(token) {
                hooks {
                    kordShutdownHook = false
                }
                presence {
                    since = Clock.System.now()
                    playing(DynamicStatus.statusForPlayerCount())
                }
                messageCommands {
                    defaultPrefix = Configurator.config[PluginSpec.BotSpec.commandPrefix]
                    invokeOnMention = true
                    check(DefaultCheck::defaultCheck)
                }
                slashCommands {
                    enabled = false
                }
                extensions {
                    DiscordBotConstants.BOT_EXTENSIONS.filter { Configurator.extensionEnabled(it) }.forEach { add(it) }

                    help {
                        paginatorTimeout = Configurator.config[PluginSpec.BotSpec.commandTimeout]
                        deletePaginatorOnTimeout = true
                        deleteInvocationOnPaginatorTimeout = true
                        colour { DISCORD_GREEN }
                    }
                }
            }
        }

        val botChannels = mutableSetOf<TextChannel>()
        val botGuilds = mutableSetOf<Guild>()
        private var job: Job? = null
    }

    suspend fun start() {
        fillBotChannels()

        job = CoroutineScope(Dispatchers.Default).launch {
            bot.start()
        }

        Logger.log("Bot ready!")
    }

    private suspend fun fillBotChannels() {
        val validBotChannels = Configurator.config[PluginSpec.BotSpec.botChannels]
        kordClient.guilds.collect { guild: Guild ->
            guild.channels.collect { channel ->
                if (
                    ClientInfo.hasChannelPermissions(channel, DiscordBotConstants.REQUIRED_PERMISSIONS)
                    && channel is TextChannel
                    && channel.name in validBotChannels
                ) {
                    botChannels.add(channel)
                    botGuilds.add(guild)
                }
            }
        }
    }

    suspend fun stop() {
        kordClient.shutdown()
        kordClient.coroutineContext.job.join()
        job?.join()
    }
}
