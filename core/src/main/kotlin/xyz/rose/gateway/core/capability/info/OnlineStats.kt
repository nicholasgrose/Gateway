package xyz.rose.gateway.core.capability.info

import xyz.rose.gateway.core.capability.Capability

/**
 * The stats of online users
 */
interface OnlineStats : Capability {
    /**
     * The online users
     */
    val online: List<String>
}
