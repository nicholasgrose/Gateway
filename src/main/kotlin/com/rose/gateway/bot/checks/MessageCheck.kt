package com.rose.gateway.bot.checks

import com.kotlindiscord.kord.extensions.checks.messageFor
import com.kotlindiscord.kord.extensions.checks.types.Check
import com.rose.gateway.bot.DiscordBot
import com.rose.gateway.configuration.Configurator
import com.rose.gateway.configuration.PluginSpec

object MessageCheck {
    val notCommand: Check<*> = {
        val messageContent = messageFor(event)?.asMessageOrNull()?.content

        when {
            messageContent == null -> fail()
            messageContent.startsWith(Configurator.config[PluginSpec.BotSpec.commandPrefix]) -> fail("Is command.")
            else -> pass()
        }
    }

    val notSelf: Check<*> = {
        val author = messageFor(event)?.asMessageOrNull()?.author

        when {
            author == null -> fail("No author found.")
            author.id == DiscordBot.getKordClient().selfId -> fail("Is self.")
            else -> pass()
        }
    }
}
