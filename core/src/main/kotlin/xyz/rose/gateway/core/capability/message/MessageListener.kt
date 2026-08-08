package xyz.rose.gateway.core.capability.message

import xyz.rose.gateway.core.capability.Capability

/**
 * A capability that can listen for messages dispatched through Gateway.
 */
interface MessageListener : Capability {
    /**
     * Called when a message is dispatched through Gateway.
     *
     * @param message The message that was dispatched
     */
    suspend fun onMessage(message: GatewayMessage)
}
