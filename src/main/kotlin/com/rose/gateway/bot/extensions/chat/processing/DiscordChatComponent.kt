package com.rose.gateway.bot.extensions.chat.processing

import guru.zoroark.lixy.LixyTokenType

enum class DiscordChatComponent : LixyTokenType {
    USER_MENTION,
    ROLE_MENTION,
    CHANNEL_MENTION,
    URL,
    TEXT
}
