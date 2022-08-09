package com.rose.gateway.discord.bot.extensions

import com.kotlindiscord.kord.extensions.extensions.Extension
import com.rose.gateway.GatewayPlugin
import org.koin.core.component.KoinComponent

interface ToggleableExtension : KoinComponent {
    fun extensionName(): String
    fun extensionConstructor(): () -> Extension
    fun isEnabled(plugin: GatewayPlugin): Boolean
}
