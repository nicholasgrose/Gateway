package com.rose.gateway.bot

import com.kotlindiscord.kord.extensions.ExtensibleBot
import com.rose.gateway.GatewayPlugin
import com.rose.gateway.Logger
import com.rose.gateway.bot.client.ClientInfo
import com.rose.gateway.bot.presence.DynamicPresence
import com.rose.gateway.configuration.PluginConfiguration
import com.rose.gateway.shared.configurations.botChannels
import com.rose.gateway.shared.configurations.botToken
import dev.kord.core.Kord
import dev.kord.core.entity.Guild
import dev.kord.core.entity.channel.TextChannel
import dev.kord.core.exception.KordInitializationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DiscordBot : KoinComponent {
    private val plugin: GatewayPlugin by inject()
    private val config: PluginConfiguration by inject()

    val botChannels = mutableSetOf<TextChannel>()
    val botGuilds = mutableSetOf<Guild>()
    private var job: Job? = null
    var botStatus = BotStatus.NOT_STARTED

    var bot = buildBot()
    val presence = DynamicPresence()

    private fun buildBot(): ExtensibleBot? {
        return try {
            if (config.notLoaded()) {
                botStatus = BotStatus.STOPPED because "No valid configuration is loaded."
                null
            } else {
                runBlocking { createBot(config.botToken()) }
            }
        } catch (e: KordInitializationException) {
            botStatus = BotStatus.STOPPED because e.localizedMessage
            null
        }
    }

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
    val clientInfo = ClientInfo()

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

        val validBotChannels = config.botChannels()
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

    private suspend fun launchBotInNewThread() {
        job = try {
            botStatus = BotStatus.RUNNING
            bot?.startAsync()
        } catch (error: KordInitializationException) {
            val message = "An error occurred while running bot: ${error.message}"
            Logger.logInfo(message)
            botStatus = BotStatus.STOPPED because message
            null
        }
    }

    suspend fun stop() {
        botStatus = BotStatus.STOPPING

        kordClient?.shutdown()
        job?.join()

        botStatus = BotStatus.STOPPED
        TODO("Not yet supported fully on bot")
    }

    fun isRunning(): Boolean = botStatus == BotStatus.RUNNING

    suspend fun rebuild() {
        stop()
        bot = buildBot()
        start()
    }

    fun restart() {
        TODO("Does not yet exist on bot")
    }
}
