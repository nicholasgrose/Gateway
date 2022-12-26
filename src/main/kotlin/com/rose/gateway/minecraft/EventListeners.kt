package com.rose.gateway.minecraft

import com.rose.gateway.GatewayPlugin
import com.rose.gateway.minecraft.chat.ActionListener
import com.rose.gateway.minecraft.chat.ChatListener
import com.rose.gateway.minecraft.chat.CommandListener
import com.rose.gateway.minecraft.chat.ConnectionListener
import com.rose.gateway.minecraft.users.UserCount
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Provides functionality for adding minecraft event listeners
 */
object EventListeners : KoinComponent {
    private val plugin: GatewayPlugin by inject()
    private val server = plugin.server

    /**
     * Registers minecraft event listeners. Should only be called once
     */
    fun registerListeners() {
        server.pluginManager.registerEvents(ActionListener(), plugin)
        server.pluginManager.registerEvents(ChatListener(), plugin)
        server.pluginManager.registerEvents(CommandListener(), plugin)
        server.pluginManager.registerEvents(ConnectionListener(), plugin)
        server.pluginManager.registerEvents(UserCount(), plugin)
    }
}
