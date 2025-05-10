package xyz.rose.gateway.capability

/**
 * Interface for capabilities that provide the ability to receive message events.
 * This capability allows receiving message events from a platform.
 */
interface MessageEventCapability : Capability {
    /**
     * Registers a callback for when a message is received.
     *
     * @param callback The callback to register. The callback takes two parameters:
     *                 - senderId: The identifier of the sender of the message.
     *                 - message: The content of the message.
     */
    fun registerMessageCallback(callback: (String, String) -> Unit)

    /**
     * Unregisters a message callback.
     *
     * @param callback The callback to unregister.
     */
    fun unregisterMessageCallback(callback: (String, String) -> Unit)
}
