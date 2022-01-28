package com.rose.gateway.bot.extensions.whitelist

import com.kotlindiscord.kord.extensions.commands.Arguments
import com.kotlindiscord.kord.extensions.commands.converters.impl.string
import dev.kord.common.annotation.KordPreview

@OptIn(KordPreview::class)
class WhitelistArguments : Arguments() {
    val username by string {
        name = "username"
        description = "The user to modify the whitelist status of."
    }
}
