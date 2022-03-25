package com.rose.gateway.minecraft.chat.processing.tokens

import guru.zoroark.lixy.LixyTokenType

enum class ChatComponent : LixyTokenType {
    USER_MENTION,
    USER_QUOTE_MENTION,
    TEXT_CHANNEL_MENTION,
    VOICE_CHANNEL_MENTION,
    ROLE_MENTION,
    ROLE_QUOTE_MENTION,
    URL,
    TEXT
}
