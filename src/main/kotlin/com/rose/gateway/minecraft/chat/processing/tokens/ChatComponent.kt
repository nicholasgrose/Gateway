package com.rose.gateway.minecraft.chat.processing.tokens

import guru.zoroark.lixy.LixyTokenType

/**
 * A token representing an individual component of the chat
 *
 * @constructor Create a chat component
 */
enum class ChatComponent : LixyTokenType {
    /**
     * Represents a user mention
     *
     * @constructor Create a user mention
     */
    USER_MENTION,

    /**
     * Represents a user quote-mention
     *
     * @constructor Create a user quote-mention
     */
    USER_QUOTE_MENTION,

    /**
     * Represents a text channel mention
     *
     * @constructor Create a text channel mention
     */
    TEXT_CHANNEL_MENTION,

    /**
     * Represents a voice channel mention
     *
     * @constructor Create a voice channel mention
     */
    VOICE_CHANNEL_MENTION,

    /**
     * Represents a role mention
     *
     * @constructor Create a role mention
     */
    ROLE_MENTION,

    /**
     * Represents a role quote-mention
     *
     * @constructor Create a role quote-mention
     */
    ROLE_QUOTE_MENTION,

    /**
     * Represents a URL
     *
     * @constructor Create a URL
     */
    URL,

    /**
     * Represents plaintext
     *
     * @constructor Create plaintext
     */
    TEXT,
}
