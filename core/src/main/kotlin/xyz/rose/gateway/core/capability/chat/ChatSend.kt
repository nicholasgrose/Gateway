package xyz.rose.gateway.core.capability.chat

import xyz.rose.gateway.core.PlatformGatewayClient
import xyz.rose.gateway.core.capability.MessageMetadataMap

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
    suspend fun send(message: ChatMessage, metadata: MessageMetadataMap = emptyMap()) {
        client.dispatch(message, metadata)
    }
}
