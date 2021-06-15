package com.rose.gateway.bot

import com.rose.gateway.bot.extensions.about.AboutExtension
import com.rose.gateway.bot.extensions.chat.ChatExtension
import com.rose.gateway.bot.extensions.list.ListExtension
import com.rose.gateway.bot.extensions.whitelist.WhitelistExtension
import com.rose.gateway.configuration.Configurator
import com.rose.gateway.configuration.PluginSpec
import dev.kord.common.entity.Permission
import dev.kord.common.entity.Permissions
import net.kyori.adventure.text.format.TextColor

object DiscordBotConstants {
    val DISCORD_COLOR by lazy { TextColor.fromHexString(Configurator.config[PluginSpec.MinecraftSpec.discordUserColor]) }
    val MENTION_COLOR by lazy { TextColor.fromHexString(Configurator.config[PluginSpec.MinecraftSpec.discordMentionColor]) }
    val BOT_EXTENSIONS = listOf(::WhitelistExtension, ::ListExtension, ::AboutExtension, ::ChatExtension)
    val REQUIRED_PERMISSIONS = Permissions(
        listOf(
            Permission.SendMessages,
            Permission.EmbedLinks,
            Permission.ManageMessages,
            Permission.AddReactions,
            Permission.ReadMessageHistory
        )
    )
    val MEMBER_QUERY_MAX by lazy { Configurator.config[PluginSpec.BotSpec.memberQueryMax] }
}
