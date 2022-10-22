package com.rose.gateway.discord.bot.extensions.chat.processing

import guru.zoroark.lixy.LixyTokenType

/**
 * Defines all the tokens that can be pulled from a Discord message
 *
 * @constructor Create a Discord chat component
 *
 * @see LixyTokenType
 */
enum class DiscordChatComponent : LixyTokenType {
    /**
     * A token representing the mention of a user
     *
     * @constructor Create a user mention token
     */
    USER_MENTION,

    /**
     * A token representing the mention of a role
     *
     * @constructor Create a role mention token
     */
    ROLE_MENTION,

    /**
     * A token representing the mention of a channel
     *
     * @constructor Create a channel mention token
     */
    CHANNEL_MENTION,

    /**
     * A token representing the mention of a URL
     *
     * @constructor Create a URL token
     */
    URL,

    /**
     * A token representing the general, unformatted text
     *
     * @constructor Create a text token
     */
    TEXT
}
