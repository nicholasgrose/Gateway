package com.rose.gateway.bot

import com.rose.gateway.bot.extensions.about.AboutExtension
import com.rose.gateway.bot.extensions.chat.ChatExtension
import com.rose.gateway.bot.extensions.list.ListExtension
import com.rose.gateway.bot.extensions.whitelist.WhitelistExtension
import dev.kord.common.entity.Permission
import dev.kord.common.entity.Permissions

object DiscordBotConstants {
    val BOT_EXTENSIONS = setOf(WhitelistExtension, ListExtension, AboutExtension, ChatExtension)
    val REQUIRED_PERMISSIONS = Permissions(
        setOf(
            Permission.SendMessages,
            Permission.EmbedLinks
        )
    )
}
