package xyz.rose.gateway.minecraft.capability

import xyz.rose.gateway.capability.MessageEventCapability

/**
 * Interface for capabilities that provide the ability to receive chat message events in Minecraft.
 * This capability allows receiving chat message events from Minecraft players.
 */
interface MinecraftChatMessageEventCapability : MessageEventCapability {
    /**
     * Registers a callback for when a chat message is received.
     *
     * @param callback The callback to register. The callback takes two parameters:
     *                 - senderId: The name of the player who sent the message.
     *                 - message: The content of the message.
     */
    override fun registerMessageCallback(callback: (String, String) -> Unit)

    /**
     * Unregisters a chat message callback.
     *
     * @param callback The callback to unregister.
     */
    override fun unregisterMessageCallback(callback: (String, String) -> Unit)
}
