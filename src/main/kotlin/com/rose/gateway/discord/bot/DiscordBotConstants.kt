package com.rose.gateway.discord.bot

import com.rose.gateway.discord.bot.extensions.about.AboutExtension
import com.rose.gateway.discord.bot.extensions.chat.ChatExtension
import com.rose.gateway.discord.bot.extensions.ip.IpExtension
import com.rose.gateway.discord.bot.extensions.list.ListExtension
import com.rose.gateway.discord.bot.extensions.tps.TpsExtension
import com.rose.gateway.discord.bot.extensions.whitelist.WhitelistExtension
import dev.kord.common.entity.Permission
import dev.kord.common.entity.Permissions

/**
 * Constants used for the Discord bot
 */
object DiscordBotConstants {
    /**
     * The maximum number of members that should be queried when querying for members
     */
    const val MEMBER_QUERY_MAX = 1

    /**
     * All the Discord bot extensions that exist in the plugin
     */
    val BOT_EXTENSIONS = setOf(
        AboutExtension,
        ChatExtension,
        IpExtension,
        ListExtension,
        TpsExtension,
        WhitelistExtension,
    )

    /**
     * The permissions the bot requires to function
     */
    val REQUIRED_PERMISSIONS = Permissions(
        setOf(
            Permission.SendMessages,
            Permission.EmbedLinks,
        ),
    )
}
