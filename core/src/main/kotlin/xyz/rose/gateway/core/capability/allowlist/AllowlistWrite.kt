package xyz.rose.gateway.core.capability.allowlist

import xyz.rose.gateway.core.PlatformGatewayClient
import xyz.rose.gateway.core.capability.MessageMetadataMap


/**
 * Gateway capability for writing an allowlist
 */
class AllowlistWrite(private val client: PlatformGatewayClient) {
    /**
     * Adds the specified ID to the allowlist.
     *
     * @param id The ID to be added to the allowlist.
     * @return The response to the operation from connected clients.
     */
    suspend fun add(id: String): Response {
        val result = client.query(AllowlistWriteAddData(id))

        return Response(result.metadata)
    }

    /**
     * Removes the specified ID from the allowlist.
     *
     * @param id The ID to be removed from the allowlist.
     * @return The response to the operation from connected clients.
     */
    suspend fun remove(id: String): Response {
        val result = client.query(AllowlistWriteRemoveData(id))

        return Response(result.metadata)
    }

    /**
     * Response to an allowlist write operation.
     *
     * @property metadata The metadata of the response.
     */
    data class Response(val metadata: MessageMetadataMap)
}
