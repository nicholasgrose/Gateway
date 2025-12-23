package xyz.rose.gateway.core.config

/**
 * The potential results of loading a Gateway config object.
 *
 * @param T The object type that was loaded
 */
sealed class GatewayConfigLoadResult<T> {
    /**
     * The constructor for the load result.
     * This is private because it should not be initialized directly.
     */
    private constructor()

    /**
     * A successful config load.
     *
     * @param T The object type that was loaded
     * @property data THe data that was loaded
     * @constructor Create a new [Success] result
     */
    data class Success<T>(val data: T) : GatewayConfigLoadResult<T>()

    /**
     * A failed config load triggered by a lack of a source.
     * This implies that a default should be used or created.
     *
     * @param T The object type requested
     * @constructor Create a new [Uninitialized] result
     */
    class Uninitialized<T> : GatewayConfigLoadResult<T>()

    /**
     * A failed config load caused by an error or invalid state.
     *
     * @param T The object type requested
     * @property throwable The error or message that triggered the failure
     * @constructor Create a new [Failure] result
     */
    data class Failure<T>(val throwable: Throwable) : GatewayConfigLoadResult<T>()
}
