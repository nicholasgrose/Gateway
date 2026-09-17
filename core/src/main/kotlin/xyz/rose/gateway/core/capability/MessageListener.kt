package xyz.rose.gateway.core.capability

import kotlin.reflect.KClass

/**
 * A capability that can listen for messages dispatched through Gateway.
 */
interface MessageListener<T : GatewayMessageData, R : GatewayMessageResponse> : Capability {
    /**
     * The type of message that this listener can handle.
     */
    val dataType: KClass<T>

    /**
     * The type of response that this listener will return.
     */
    val response: KClass<R>

    /**
     * Called when a message is dispatched through Gateway.
     *
     * @param message The message that was dispatched
     */
    suspend fun onMessage(message: GatewayMessage<T>): R
}

/**
 * Marker interface for data that can be sent in response to a [GatewayMessage].
 */
interface GatewayMessageResponse

/**
 * A simple message response for whether the message was handled successfully.
 *
 * @property success Whether the operation was successful
 */
data class SuccessResponse(val success: Boolean) : GatewayMessageResponse
