package xyz.rose.gateway.minecraft.capability

import xyz.rose.gateway.capability.SendMessageCapability

/**
 * Interface for capabilities that provide the ability to send messages in Minecraft.
 * This capability allows sending chat messages to Minecraft players.
 */
interface MinecraftSendMessageCapability : SendMessageCapability {
    /**
     * Sends a chat message to all players.
     *
     * @param message The message to send.
     */
    override fun broadcastMessage(message: String)

    /**
     * Sends a chat message to a specific player.
     *
     * @param recipientId The name of the player to send the message to.
     * @param message The message to send.
     * @return True if the message was sent, false if the player is not online.
     */
    override fun sendMessageToRecipient(recipientId: String, message: String): Boolean
}
