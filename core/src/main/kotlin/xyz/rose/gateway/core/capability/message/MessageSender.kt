package xyz.rose.gateway.core.capability.message

import xyz.rose.gateway.core.capability.Capability

/**
 * Indicates that a Gateway platform can dispatch messages.
 */
interface MessageSender : Capability {
    /**
     * Registers a listener for messages
     *
     * @param listener The listener to register
     */
    fun registerListener(listener: Listener)

    /**
     * Unregisters a listener for messages
     *
     * @param listener The listener to unregister
     */
    fun unregisterListener(listener: Listener)

    /**
     * A listener for messages
     *
     * @constructor Create an empty Listener
     */
    interface Listener {
        /**
         * Called when a message is received
         *
         * @param message The message
         */
        fun onMessage(message: Message)
    }
}
