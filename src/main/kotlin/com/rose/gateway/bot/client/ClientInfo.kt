package com.rose.gateway.bot.client

import com.kotlindiscord.kord.extensions.utils.permissionsForMember
import com.rose.gateway.bot.DiscordBot
import dev.kord.common.entity.Permissions
import dev.kord.core.entity.channel.GuildChannel

object ClientInfo {
    suspend fun hasChannelPermissions(channel: GuildChannel, permissions: Permissions): Boolean {
        val channelPermissions = getPermissionsForChannel(channel)
        return permissions in channelPermissions
    }

    private suspend fun getPermissionsForChannel(channel: GuildChannel): Permissions {
        return channel.permissionsForMember(DiscordBot.getKordClient()!!.selfId)
    }
}
