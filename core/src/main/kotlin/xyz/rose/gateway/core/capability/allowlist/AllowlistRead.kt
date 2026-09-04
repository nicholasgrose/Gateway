package xyz.rose.gateway.core.capability.allowlist

import xyz.rose.gateway.core.PlatformGatewayClient
import xyz.rose.gateway.core.capability.MessageMetadataMap

/**
 * Gateway capability for reading an allowlist
 */
class AllowlistRead(private val client: PlatformGatewayClient) {
    /**
     * Query the allowlist from connected platforms.
     *
     * @return The response to the allowlist read operation.
     */
    suspend fun query(): Response {
        val result = client.query(AllowlistReadRequestData())

        return Response(result.metadata)
    }

    /**
     * Response to an allowlist read operation.
     *
     * @property metadata The metadata of the response from connected platforms.
     */
    data class Response(val metadata: MessageMetadataMap)
}
