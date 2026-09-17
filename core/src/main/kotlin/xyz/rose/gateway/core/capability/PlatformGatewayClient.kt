package xyz.rose.gateway.core.capability

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
    suspend fun <T : GatewayMessageData, R : GatewayMessageResponse> dispatch(
        data: T,
        metadata: MessageMetadataMap = emptyMap()
    ): PlatformMap<R> {
        val message = GatewayMessage(
            sourcePlatformType = definition.type,
            sourcePlatformId = definition.uid,
            data = data,
            metadata = metadata
        )

        return dispatcher.dispatch(message)
    }
}
