package xyz.rose.gateway.core.capability

/**
 * Gateway capability for reading an allowlist
 */
interface AllowlistRead : GatewayCapability {
    /**
     * The allowlist of IDs
     */
    val allowlist: List<String>
}
