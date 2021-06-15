package com.rose.gateway.bot.checks

import com.kotlindiscord.kord.extensions.checks.messageFor
import com.rose.gateway.bot.DiscordBot
import com.rose.gateway.configuration.Configurator
import com.rose.gateway.configuration.PluginSpec
import dev.kord.core.event.Event

object MessageCheck {
    suspend fun notCommand(event: Event): Boolean {
        val messageContent = messageFor(event)?.asMessageOrNull()?.content ?: return false
        return !messageContent.startsWith(Configurator.config[PluginSpec.BotSpec.commandPrefix])
    }

    suspend fun notSelf(event: Event): Boolean {
        val author = messageFor(event)?.asMessageOrNull()?.author ?: return false
        return author.id != DiscordBot.kordClient.selfId
    }
}
