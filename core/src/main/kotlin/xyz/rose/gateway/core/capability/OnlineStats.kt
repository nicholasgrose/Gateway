package xyz.rose.gateway.core.capability

/**
 * The stats of online users
 */
interface OnlineStats : GatewayCapability {
    /**
     * The online users
     */
    val online: List<String>
}
