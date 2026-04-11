package xyz.rose.gateway.core.capability

/**
 * A capability that can receive messages from other platforms
 *
 * @param T The type of message to receive
 */
interface MessageReceiver<T> : GatewayCapability {
    /**
     * Sends a message to this platform
     *
     * @param message The message to send
     */
    fun sendMessage(message: T)
}
