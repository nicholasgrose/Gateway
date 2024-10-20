package com.rose.gateway.discord.bot.extensions.whitelist

import dev.kordex.core.commands.Arguments
import dev.kordex.core.commands.converters.impl.string

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
