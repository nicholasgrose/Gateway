package xyz.rose.gateway.core.config

import kotlinx.serialization.KSerializer
import kotlin.reflect.KClass

/**
 * Provides the schema for a Gateway configuration.
 *
 * @constructor Create a new Gateway config schema
 */
interface GatewayConfigSchema<T : Any> {
    /**
     * The unique key for the configuration object.
     */
    val key: String

    /**
     * The serializer for the configuration object.
     */
    val serializer: KSerializer<T>

    /**
     * The type that can be injected as the configuration object from Koin.
     */
    val injectableType: KClass<T>

    /**
     * The default values for this config to use in case the config was unable to be loaded.
     */
    val default: T
}
