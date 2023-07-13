package com.rose.gateway.minecraft.whitelist

/**
 * The state of a whitelist as a result of modification
 *
 * @constructor Create a whitelist state
 */
enum class WhitelistState {
    /**
     * Represents that the whitelist was successfully modified
     *
     * @constructor Create a modified state
     */
    STATE_MODIFIED,

    /**
     * Represents that the whitelist was successfully modified
     *
     * @constructor Create a invalid state
     */
    STATE_INVALID,

    /**
     * Represents that the whitelist was sustained
     *
     * @constructor Create a sustained state
     */
    STATE_SUSTAINED,
}
