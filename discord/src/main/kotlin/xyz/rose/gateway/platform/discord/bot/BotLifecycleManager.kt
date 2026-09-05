package xyz.rose.gateway.platform.discord.bot

import dev.kord.core.Kord
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.core.on
import dev.kord.gateway.Intent
import dev.kord.gateway.Intents
import dev.kord.gateway.PrivilegedIntent
import kotlinx.coroutines.coroutineScope
import xyz.rose.gateway.platform.discord.config.DiscordConfig

class BotLifecycleManager(val config: DiscordConfig) {
    var bot: Kord? = null

    suspend fun start(): Boolean = coroutineScope {
        if (bot != null) return@coroutineScope false

        val constructedBot = Kord(config.botToken)
        configureBot(constructedBot)
        constructedBot.login {
            @OptIn(PrivilegedIntent::class)
            intents += Intents(
                Intent.MessageContent,
                Intent.GuildMembers
            )
        }
        bot = constructedBot

        return@coroutineScope true
    }

    private suspend fun configureBot(bot: Kord) {
        configureMessageListeners(bot)
        configureAllowlistCommands(bot)
        configureInfoCommands(bot)
    }

    private fun configureMessageListeners(bot: Kord) {
        bot.on<MessageCreateEvent> {}
    }

    private suspend fun configureAllowlistCommands(bot: Kord) {
        bot.createGlobalChatInputCommand(
            name = "allowlist",
            description = "Modify the allowlist"
        ) {}
    }

    private suspend fun configureInfoCommands(bot: Kord) {
        bot.createGlobalChatInputCommand(
            name = "info",
            description = "Get information about the bot"
        ) {}
    }

    suspend fun stop(): Boolean = coroutineScope {
        if (bot == null) return@coroutineScope false

        bot?.logout()
        bot = null

        return@coroutineScope true
    }
}
