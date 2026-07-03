package xyz.rose.gateway.core.capability.allowlist

import xyz.rose.gateway.core.capability.Capability

/**
 * Gateway capability for writing an allowlist
 */
interface AllowlistWrite : Capability {
    /**
     * Adds the specified ID to the allowlist.
     *
     * @param id The ID to be added to the allowlist.
     * @return Whether the operation succeeded
     */
    fun add(id: String): Boolean

    /**
     * Removes the specified ID from the allowlist.
     *
     * @param id The ID to be removed from the allowlist.
     * @return Whether the operation succeeded
     */
    fun remove(id: String): Boolean
}
