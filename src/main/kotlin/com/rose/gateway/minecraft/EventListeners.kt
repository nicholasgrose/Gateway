package com.rose.gateway.minecraft

import com.rose.gateway.GatewayPlugin
import com.rose.gateway.configuration.Configurator
import com.rose.gateway.configuration.PluginSpec
import com.rose.gateway.minecraft.chat.ChatListener
import com.rose.gateway.minecraft.users.UserCount
import org.bukkit.Server

object EventListeners {
    fun registerListeners(server: Server) {
        if (Configurator.config[PluginSpec.BotSpec.EnabledExtensionsSpec.chatExtension]) {
            server.pluginManager.registerEvents(ChatListener, GatewayPlugin.plugin)
        }
        server.pluginManager.registerEvents(UserCount, GatewayPlugin.plugin)
    }
}
