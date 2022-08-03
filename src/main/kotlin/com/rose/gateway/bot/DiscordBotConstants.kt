package com.rose.gateway.bot

import com.rose.gateway.bot.extensions.about.AboutExtension
import com.rose.gateway.bot.extensions.chat.ChatExtension
import com.rose.gateway.bot.extensions.ip.IpExtension
import com.rose.gateway.bot.extensions.list.ListExtension
import com.rose.gateway.bot.extensions.whitelist.WhitelistExtension
import dev.kord.common.entity.Permission
import dev.kord.common.entity.Permissions

object DiscordBotConstants {
    const val MEMBER_QUERY_MAX = 1
    val BOT_EXTENSIONS = setOf(AboutExtension, ChatExtension, IpExtension, ListExtension, WhitelistExtension)
    val REQUIRED_PERMISSIONS = Permissions(
        setOf(
            Permission.SendMessages,
            Permission.EmbedLinks
        )
    )
}
