package com.rose.gateway.discord.bot.extensions.whitelist

import dev.kordex.core.commands.Arguments
import dev.kordex.core.commands.converters.impl.string
import gateway.i18n.Translations

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
        name = Translations.Commands.Whitelist.Args.Username.name
        description = Translations.Commands.Whitelist.Args.Username.description
    }
}
