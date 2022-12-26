package com.rose.gateway.discord.bot.extensions.whitelist

import com.kotlindiscord.kord.extensions.commands.Arguments
import com.kotlindiscord.kord.extensions.commands.converters.impl.string

/**
 * Arguments for whitelist-related Discord commands
 *
 * @constructor Create an instance of the whitelist arguments
 */
class WhitelistArguments : Arguments() {
    /**
     * The username specified
     */
    val username by string {
        name = "username"
        description = "The user to modify the whitelist status of."
    }
}
