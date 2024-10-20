package com.rose.gateway.discord.bot.checks

import com.rose.gateway.discord.bot.DiscordBotController
import dev.kord.core.Kord
import dev.kordex.core.checks.channelFor
import dev.kordex.core.checks.messageFor
import dev.kordex.core.checks.types.Check
import gateway.i18n.Translations
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Provides event checks for individual Discord messages
 *
 * @see Check
 */
object MessageCheck : KoinComponent {
    private val bot: DiscordBotController by inject()

    /**
     * Check that checks that the event is in a valid bot channel
     */
    val isValidBotChannel: Check<*> = {
        val channelBehaviour = channelFor(event)
        val channel = channelBehaviour?.asChannelOrNull()

        if (bot.state.botChannels.contains(channel)) {
            pass()
        } else {
            fail(Translations.Checks.Chat.Channel.NotBotChannel.fail)
        }
    }

    /**
     * Check that checks that the message was not sent by the bot, itself
     */
    val notSelf: Check<*> = {
        val kordClient = getKoin().get<Kord>()
        val author = messageFor(event)?.asMessageOrNull()?.author

        when {
            author == null -> fail(Translations.Checks.Chat.Self.NoAuthor.fail)
            author.id == kordClient.selfId -> fail(Translations.Checks.Chat.Self.OwnId.fail)
            else -> pass()
        }
    }
}
