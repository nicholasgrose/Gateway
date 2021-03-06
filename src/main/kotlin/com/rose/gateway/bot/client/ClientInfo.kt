package com.rose.gateway.bot.client

import com.kotlindiscord.kord.extensions.utils.permissionsForMember
import com.rose.gateway.GatewayPlugin
import dev.kord.common.entity.Permissions
import dev.kord.core.entity.channel.GuildChannel

class ClientInfo(private val plugin: GatewayPlugin) {
    suspend fun hasChannelPermissions(channel: GuildChannel, permissions: Permissions): Boolean {
        val channelPermissions = getPermissionsForChannel(channel)
        return permissions in channelPermissions
    }

    private suspend fun getPermissionsForChannel(channel: GuildChannel): Permissions {
        return channel.permissionsForMember(plugin.discordBot.kordClient!!.selfId)
    }
}
