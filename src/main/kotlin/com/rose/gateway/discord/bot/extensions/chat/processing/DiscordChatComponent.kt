package com.rose.gateway.discord.bot.extensions.chat.processing

import guru.zoroark.lixy.LixyTokenType

enum class DiscordChatComponent : LixyTokenType {
    USER_MENTION,
    ROLE_MENTION,
    CHANNEL_MENTION,
    URL,
    TEXT
}
