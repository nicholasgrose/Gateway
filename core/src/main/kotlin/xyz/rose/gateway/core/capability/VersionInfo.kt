package xyz.rose.gateway.core.capability

/**
 * Platform version information
 */
interface VersionInfo : GatewayCapability {
    /**
     * The version of the platform
     */
    val version: String
}
