package com.rose.gateway.bot.message

import com.kotlindiscord.kord.extensions.utils.respond
import dev.kord.core.entity.Message
import dev.kord.rest.builder.message.create.MessageCreateBuilder
import kotlinx.coroutines.delay

object MessageLifetime {
    private suspend fun deleteAfterDelay(delay: Long, vararg messages: Message) {
        delay(delay)
        messages.forEach { it.delete() }
    }

    suspend fun Message.respondWithLifetime(delay: Long, builder: suspend MessageCreateBuilder.() -> Unit) {
        val response = this.respond {
            builder()
        }
        deleteAfterDelay(delay, this, response)
    }
}
