package xyz.rose.gateway.core.config

/**
 * The result of saving a Gateway config
 *
 * @constructor Create a new Gateway config save result
 */
sealed class GatewayConfigSaveResult {
    /**
     * A successful save operation
     *
     * @constructor Create a new successful save result
     */
    class Success : GatewayConfigSaveResult()

    /**
     * A failed save operation
     *
     * @property cause The cause of the failure
     * @constructor Create a new failed save result
     */
    class Failure(val cause: Throwable) : GatewayConfigSaveResult()
}
