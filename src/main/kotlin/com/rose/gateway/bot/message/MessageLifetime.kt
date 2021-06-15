package com.rose.gateway.bot.message

import com.kotlindiscord.kord.extensions.utils.respond
import com.rose.gateway.configuration.Configurator
import com.rose.gateway.configuration.PluginSpec
import dev.kord.core.entity.Message
import dev.kord.rest.builder.message.MessageCreateBuilder
import kotlinx.coroutines.delay

object MessageLifetime {
    private suspend fun deleteAfterDelay(vararg messages: Message) {
        delay(Configurator.config[PluginSpec.BotSpec.commandTimeout])
        messages.forEach { it.delete() }
    }

    suspend fun Message.respondWithLifetime(builder: suspend MessageCreateBuilder.() -> Unit) {
        val response = this.respond {
            builder()
        }
        deleteAfterDelay(this, response)
    }
}
