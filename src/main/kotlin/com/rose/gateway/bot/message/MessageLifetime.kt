package com.rose.gateway.bot.message

import com.kotlindiscord.kord.extensions.utils.respond
import dev.kord.core.behavior.channel.MessageChannelBehavior
import dev.kord.core.behavior.channel.createMessage
import dev.kord.core.entity.Message
import dev.kord.rest.builder.message.create.MessageCreateBuilder
import kotlinx.coroutines.delay

object MessageLifetime {
    private suspend fun deleteAfterDelay(delay: Long, message: Message) {
        delay(delay)
        message.delete()
    }

    suspend fun MessageChannelBehavior.createMessageWithLifetime(delay: Long, builder: suspend MessageCreateBuilder.() -> Unit) {
        val response = this.createMessage {
            builder()
        }
        deleteAfterDelay(delay, response)
    }
}
