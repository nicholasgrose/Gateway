package com.rose.gateway.bot.extensions

import com.kotlindiscord.kord.extensions.extensions.Extension
import com.rose.gateway.GatewayPlugin

interface ToggleableExtension {
    fun extensionName(): String
    fun extensionConstructor(): () -> Extension
    fun isEnabled(plugin: GatewayPlugin): Boolean
}
