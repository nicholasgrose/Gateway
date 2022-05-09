package com.rose.gateway.bot

import com.kotlindiscord.kord.extensions.ExtensibleBot
import com.rose.gateway.GatewayPlugin
import com.rose.gateway.Logger
import com.rose.gateway.bot.client.ClientInfo
import com.rose.gateway.bot.presence.DynamicPresence
import com.rose.gateway.shared.configurations.BotConfiguration.botChannels
import com.rose.gateway.shared.configurations.BotConfiguration.botToken
import dev.kord.core.Kord
import dev.kord.core.entity.Guild
import dev.kord.core.entity.channel.TextChannel
import dev.kord.core.exception.KordInitializationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class DiscordBot(private val plugin: GatewayPlugin) {
    val botChannels = mutableSetOf<TextChannel>()
    val botGuilds = mutableSetOf<Guild>()
    private var job: Job? = null
    var botStatus = BotStatus.NOT_STARTED

    val bot = try {
        if (plugin.configuration.notLoaded()) {
            botStatus = BotStatus.STOPPED because "No valid configuration is loaded."
            null
        } else {
            runBlocking { createBot(plugin.configuration.botToken()) }
        }
    } catch (e: KordInitializationException) {
        botStatus = BotStatus.STOPPED because e.localizedMessage
        null
    }
    val presence = DynamicPresence(plugin)

    private suspend fun createBot(token: String): ExtensibleBot {
        return ExtensibleBot(token) {
            hooks {
                kordShutdownHook = false
            }
            presence {
                since = plugin.startTime
                playing(presence.presenceForPlayerCount())
            }
            applicationCommands {
                enabled = true
            }
            extensions {
                extensions.addAll(
                    DiscordBotConstants.BOT_EXTENSIONS.map { extension -> extension.extensionConstructor() }
                )
            }
        }
    }

    val kordClient = bot?.getKoin()?.get<Kord>()
    private val clientInfo = ClientInfo(plugin)

    suspend fun start() {
        if (bot == null) return

        botStatus = BotStatus.STARTING

        unloadDisabledExtensions()
        fillBotChannels()
        launchBotInNewThread()

        Logger.logInfo("Bot ready!")
    }

    private suspend fun unloadDisabledExtensions() {
        for (extension in DiscordBotConstants.BOT_EXTENSIONS) {
            if (!extension.isEnabled(plugin)) bot!!.unloadExtension(extension.extensionName())
        }
    }

    suspend fun fillBotChannels() {
        botChannels.clear()
        botGuilds.clear()

        val validBotChannels = plugin.configuration.botChannels()
        kordClient?.guilds?.collect { guild ->
            guild.channels.collect { channel ->
                if (
                    clientInfo.hasChannelPermissions(channel, DiscordBotConstants.REQUIRED_PERMISSIONS) &&
                    channel is TextChannel &&
                    channel.name in validBotChannels
                ) {
                    botChannels.add(channel)
                    botGuilds.add(guild)
                }
            }
        }
    }

    private fun launchBotInNewThread() {
        job = CoroutineScope(Dispatchers.Default).launch {
            try {
                botStatus = BotStatus.RUNNING
                bot!!.start()
            } catch (error: KordInitializationException) {
                val message = "An error occurred while running bot: ${error.message}"
                Logger.logInfo(message)
                botStatus = BotStatus.STOPPED because message
            }
        }
    }

    suspend fun stop() {
        botStatus = BotStatus.STOPPING

        kordClient?.shutdown()
        job?.join()

        botStatus = BotStatus.STOPPED
    }
}
