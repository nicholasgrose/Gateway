package xyz.rose.gateway.capability

/**
 * Interface for capabilities that provide the ability to send messages.
 * This capability allows sending messages to a platform.
 */
interface SendMessageCapability : Capability {
    /**
     * Sends a message to all recipients.
     *
     * @param message The message to send.
     */
    fun broadcastMessage(message: String)

    /**
     * Sends a message to a specific recipient.
     *
     * @param recipientId The identifier of the recipient to send the message to.
     * @param message The message to send.
     * @return True if the message was sent, false if the recipient is not available.
     */
    fun sendMessageToRecipient(recipientId: String, message: String): Boolean
}
