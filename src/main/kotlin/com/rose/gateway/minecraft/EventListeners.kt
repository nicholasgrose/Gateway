package com.rose.gateway.minecraft

import com.rose.gateway.GatewayPlugin
import com.rose.gateway.minecraft.chat.ChatListener
import com.rose.gateway.minecraft.users.UserCount
import org.bukkit.Server

class EventListeners(val plugin: GatewayPlugin) {
    fun registerListeners(server: Server) {
        server.pluginManager.registerEvents(ChatListener(plugin), plugin)
        server.pluginManager.registerEvents(UserCount(plugin), plugin)
    }
}
