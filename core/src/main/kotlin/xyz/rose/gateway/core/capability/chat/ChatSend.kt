package xyz.rose.gateway.core.capability.chat

import xyz.rose.gateway.core.capability.MessageMetadataMap
import xyz.rose.gateway.core.capability.PlatformGatewayClient
import xyz.rose.gateway.core.capability.PlatformMap
import xyz.rose.gateway.core.capability.SuccessResponse

/**
 * Sends a chat message.
 */
class ChatSend(private val client: PlatformGatewayClient) {
    /**
     * Sends a chat message to all connected platforms.
     *
     * @param message The message to send.
     * @param metadata The metadata to send with the message.
     */
    suspend fun send(message: ChatMessage, metadata: MessageMetadataMap = emptyMap()): PlatformMap<SuccessResponse> {
        return client.dispatch(message, metadata)
    }
}
