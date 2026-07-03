package xyz.rose.gateway.core.capability.info

import xyz.rose.gateway.core.capability.Capability

/**
 * Platform version information
 */
interface VersionInfo : Capability {
    /**
     * The version of the platform
     */
    val version: String
}
