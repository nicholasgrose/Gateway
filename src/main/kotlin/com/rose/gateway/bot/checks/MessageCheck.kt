package com.rose.gateway.bot.checks

import com.kotlindiscord.kord.extensions.checks.channelFor
import com.kotlindiscord.kord.extensions.checks.messageFor
import com.kotlindiscord.kord.extensions.checks.types.Check
import com.rose.gateway.GatewayPlugin
import com.rose.gateway.shared.configurations.BotConfiguration.commandPrefix
import dev.kord.core.Kord

class MessageCheck(val plugin: GatewayPlugin) {
    val isConfiguredBotChannel: Check<*> = {
        val channelBehaviour = channelFor(event)
        val channel = channelBehaviour?.asChannelOrNull()

        if (plugin.discordBot.botChannels.contains(channel)) pass()
        else fail("Channel is not configured bot channel.")
    }

    val notSelf: Check<*> = {
        val kordClient = getKoin().get<Kord>()
        val author = messageFor(event)?.asMessageOrNull()?.author

        when {
            author == null -> fail("No author found.")
            author.id == kordClient.selfId -> fail("Is self.")
            else -> pass()
        }
    }
}
