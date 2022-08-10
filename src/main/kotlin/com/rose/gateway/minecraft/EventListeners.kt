package com.rose.gateway.minecraft

import com.rose.gateway.GatewayPlugin
import com.rose.gateway.minecraft.chat.ChatListener
import com.rose.gateway.minecraft.users.UserCount
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Provides functionality for adding minecraft event listeners.
 */
object EventListeners : KoinComponent {
    val plugin: GatewayPlugin by inject()
    val server = plugin.server

    /**
     * Registers minecraft event listeners. Should only be called once.
     */
    fun registerListeners() {
        server.pluginManager.registerEvents(ChatListener(), plugin)
        server.pluginManager.registerEvents(UserCount(), plugin)
    }
}
