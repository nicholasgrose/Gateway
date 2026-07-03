package xyz.rose.gateway.core.capability.message

import xyz.rose.gateway.core.capability.Capability

/**
 * A capability that can receive messages from other platforms
 */
interface MessageReceiver : Capability {
    /**
     * Sends a message to this platform
     *
     * @param message The message to send
     */
    fun sendMessage(message: Message)
}
