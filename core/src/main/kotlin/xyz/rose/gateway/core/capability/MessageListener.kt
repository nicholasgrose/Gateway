package xyz.rose.gateway.core.capability

import kotlin.reflect.KClass

/**
 * A capability that can listen for messages dispatched through Gateway.
 */
interface MessageListener<T : GatewayMessageData> : Capability {
    /**
     * The type of message that this listener can handle.
     */
    val dataType: KClass<T>

    /**
     * Called when a message is dispatched through Gateway.
     *
     * @param message The message that was dispatched
     */
    suspend fun onMessage(message: GatewayMessage<T>)
}
