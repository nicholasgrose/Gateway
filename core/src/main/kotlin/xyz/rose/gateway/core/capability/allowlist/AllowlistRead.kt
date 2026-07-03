package xyz.rose.gateway.core.capability.allowlist

import xyz.rose.gateway.core.capability.Capability

/**
 * Gateway capability for reading an allowlist
 */
interface AllowlistRead : Capability {
    /**
     * The allowlist of IDs
     */
    val allowlist: List<String>
}
