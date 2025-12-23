package xyz.rose.gateway.core.config

import kotlinx.serialization.KSerializer

/**
 * A wrapper around the specifics of loading a config object.
 *
 * @constructor Create a new Gateway config file
 */
interface GatewayConfigSource<T> {
    /**
     * Loads some data from this config source.
     *
     * @param serializer The serializer to use for loading a type from the source
     * @return The loaded object or null, if it couldn't be loaded
     */
    fun load(serializer: KSerializer<T>): GatewayConfigLoadResult<T>

    /**
     * Saves some data to this config source.
     *
     * @param serializer The serializer to use for saving the data to the source
     * @param data The data to save
     * @return The result of saving the data
     */
    fun save(serializer: KSerializer<T>, data: T): GatewayConfigSaveResult
}
