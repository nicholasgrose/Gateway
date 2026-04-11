package xyz.rose.gateway.core.capability

/**
 * Indicates that a Gateway platform can dispatch messages.
 */
interface MessageSender<T> : GatewayCapability {
    /**
     * Registers a listener for messages
     *
     * @param listener The listener to register
     */
    fun registerListener(listener: Listener<T>)

    /**
     * Unregisters a listener for messages
     *
     * @param listener The listener to unregister
     */
    fun unregisterListener(listener: Listener<T>)

    /**
     * A listener for messages
     *
     * @param T The type of the message
     * @constructor Create an empty Listener
     */
    interface Listener<T> {
        /**
         * Called when a message is received
         *
         * @param message The message
         */
        fun onMessage(message: T)
    }
}
