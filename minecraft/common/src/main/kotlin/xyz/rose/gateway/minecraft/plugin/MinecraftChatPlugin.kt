package xyz.rose.gateway.minecraft.plugin

import xyz.rose.gateway.capability.Capability
import xyz.rose.gateway.minecraft.capability.MinecraftChatMessageEventCapability
import xyz.rose.gateway.minecraft.capability.MinecraftSendMessageCapability
import xyz.rose.gateway.plugin.Plugin
import xyz.rose.gateway.registry.Registrar

/**
 * Interface for Minecraft chat plugins.
 * This plugin provides functionality for sending and receiving chat messages.
 */
interface MinecraftChatPlugin : Plugin, Registrar<Capability> {
    /**
     * The capability for sending chat messages.
     */
    val sendMessageCapability: MinecraftSendMessageCapability

    /**
     * The capability for receiving chat message events.
     */
    val chatMessageEventCapability: MinecraftChatMessageEventCapability
}
