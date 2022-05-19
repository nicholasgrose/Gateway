package com.rose.gateway.bot.client

import com.kotlindiscord.kord.extensions.utils.permissionsForMember
import com.rose.gateway.bot.DiscordBot
import dev.kord.common.entity.Permissions
import dev.kord.core.entity.channel.GuildChannel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ClientInfo : KoinComponent {
    val bot: DiscordBot by inject()

    suspend fun hasChannelPermissions(channel: GuildChannel, permissions: Permissions): Boolean {
        val channelPermissions = getPermissionsForChannel(channel)
        return permissions in channelPermissions
    }

    private suspend fun getPermissionsForChannel(channel: GuildChannel): Permissions {
        return channel.permissionsForMember(bot.kordClient!!.selfId)
    }
}
