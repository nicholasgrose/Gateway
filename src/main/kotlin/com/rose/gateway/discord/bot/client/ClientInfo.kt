package com.rose.gateway.discord.bot.client

import com.kotlindiscord.kord.extensions.utils.permissionsForMember
import com.rose.gateway.discord.bot.DiscordBotController
import dev.kord.common.entity.Permissions
import dev.kord.core.entity.channel.GuildChannel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Provides info about the bot's Kord client
 *
 * @constructor Create empty Client info
 */
object ClientInfo : KoinComponent {
    private val bot: DiscordBotController by inject()

    /**
     * Determines whether the bot has the correct permissions for a channel
     *
     * @param channel The channel to check the permissions for
     * @param permissions The permissions to check for the existence of
     * @return Whether the permissions exist for the given channel
     */
    suspend fun hasChannelPermissions(
        channel: GuildChannel,
        permissions: Permissions,
    ): Boolean {
        val channelPermissions = permissionsForChannel(channel)
        return permissions in channelPermissions
    }

    /**
     * Gets all the permissions the bot has for a particular channel
     *
     * @param channel The channel to check the permissions for
     * @return The permissions the bot has in the given channel
     */
    private suspend fun permissionsForChannel(channel: GuildChannel): Permissions =
        channel.permissionsForMember(bot.discordBot.kordClient()!!.selfId)
}
