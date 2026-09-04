package xyz.rose.gateway.core.capability.allowlist

import xyz.rose.gateway.core.capability.GatewayMessageData

/**
 * A request to write to the allowlist.
 *
 * @property id The user ID to write to the allowlist.
 */
interface AllowlistWriteRequestData : GatewayMessageData {
    val id: String
}

/**
 * A request to add the user to the allowlist.
 */
class AllowlistWriteAddData(override val id: String) : AllowlistWriteRequestData

/**
 * A request to remove the user from the allowlist.
 */
class AllowlistWriteRemoveData(override val id: String) : AllowlistWriteRequestData
