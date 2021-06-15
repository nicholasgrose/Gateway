package com.rose.gateway.bot.extensions.whitelist

import com.kotlindiscord.kord.extensions.commands.converters.impl.string
import com.kotlindiscord.kord.extensions.commands.parser.Arguments
import dev.kord.common.annotation.KordPreview

@OptIn(KordPreview::class)
class WhitelistArguments : Arguments() {
    val username by string("username", "The user to modify the whitelist status of.")
}
