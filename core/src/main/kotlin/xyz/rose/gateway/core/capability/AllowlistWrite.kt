package xyz.rose.gateway.core.capability

/**
 * Gateway capability for writing an allowlist
 */
interface AllowlistWrite : GatewayCapability {
    /**
     * Adds the specified ID to the allowlist.
     *
     * @param id The ID to be added to the allowlist.
     */
    fun add(id: String)

    /**
     * Removes the specified ID from the allowlist.
     *
     * @param id The ID to be removed from the allowlist.
     */
    fun remove(id: String)
}
