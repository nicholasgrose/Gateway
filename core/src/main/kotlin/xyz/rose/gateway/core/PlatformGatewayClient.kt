package xyz.rose.gateway.core

import xyz.rose.gateway.core.capability.GatewayDispatcher
import xyz.rose.gateway.core.capability.GatewayMessage
import xyz.rose.gateway.core.capability.GatewayMessageData
import xyz.rose.gateway.core.capability.MessageMetadataMap
import xyz.rose.gateway.core.platform.GatewayPlatformDefinition

/**
 * A client for a client to interact with a gateway platform.
 */
class PlatformGatewayClient(
    private val definition: GatewayPlatformDefinition<*>,
    private val dispatcher: GatewayDispatcher
) {
    /**
     * Dispatches a message to Gateway for broadcast.
     *
     * @param T The type of the message data.
     * @param data The data to be sent.
     * @param metadata Additional metadata for the message.
     */
    suspend fun <T : GatewayMessageData> dispatch(data: T, metadata: MessageMetadataMap = emptyMap()) {
        val message = GatewayMessage(
            sourcePlatformType = definition.type,
            sourcePlatformId = definition.uid,
            data = data,
            metadata = metadata
        )

        dispatcher.dispatch(message)
    }

    /**
     * Queries Gateway for the metadata for a message.
     *
     * @param T The type of the message data.
     * @param data The data to be sent.
     * @return The response message.
     */
    suspend fun <T : GatewayMessageData> query(data: T, metadata: MessageMetadataMap = emptyMap()): GatewayMessage<T> {
        val message = GatewayMessage(
            sourcePlatformType = definition.type,
            sourcePlatformId = definition.uid,
            data = data,
            metadata = metadata
        )

        return dispatcher.enrich(message)
    }
}
