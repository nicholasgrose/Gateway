package com.rose.gateway.minecraft.chat

import net.kyori.adventure.text.Component
import org.bukkit.Bukkit

object SendMessage {
    fun sendDiscordMessage(message: Component) {
        Bukkit.getServer().broadcast(message)
    }
}
