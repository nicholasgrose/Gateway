package xyz.rose.gateway.core.config

import io.github.oshai.kotlinlogging.KLogger
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import xyz.rose.gateway.core.platform.GatewayPlatformDefinition
import java.io.IOException

/**
 * The config for the embedded Gateway app
 *
 * @property source The config source
 * @property platforms The platforms to load config for
 * @property logger The logger for Gateway
 * @property serializer The serializer for the config source (this should only be used for testing)
 * @constructor Create a new embedded Gateway config
 */
class CombinedGatewayConfig(
    val source: GatewayConfigSource<Map<String, Any>>,
    platforms: Collection<GatewayPlatformDefinition<Any>>,
    logger: KLogger,
    serializer: KSerializer<Map<String, Any>>? = null
) : GatewayConfig {
    /**
     * A schema paired with its loaded data from the config source
     *
     * @property schema The schema for the data
     * @property data The data loaded from the config source
     * @constructor Create a new loaded schema
     */
    data class LoadedSchema(val schema: GatewayConfigSchema<Any>, val data: Any)

    /**
     * The map of loaded schemas and their data
     */
    val loadedConfig: Map<String, LoadedSchema> = run {
        val schemas = platforms.map { it.schema }.associateBy { it.key }
        val serializer = serializer ?: CombinedGatewaySchemaSerializer(schemas)

        when (val loadResult = source.load(serializer)) {
            is GatewayConfigLoadResult.Success<Map<String, Any>> -> loadResult.data.mapValues {
                LoadedSchema(
                    schemas[it.key] ?: throw IllegalArgumentException("No schema found for loaded key: ${it.key}"),
                    it.value
                )
            }

            is GatewayConfigLoadResult.Uninitialized -> schemas.mapValues { LoadedSchema(it.value, it.value.default) }
                .also { schemas ->
                    // If the config was found to be uninitialized, we will save off the default values for the future
                    try {
                        source.save(serializer, schemas.mapValues { it.value.data })
                    } catch (e: IOException) {
                        logger.error(e) { "Failed to save config to source: ${e.message}" }
                    } catch (e: SerializationException) {
                        logger.error(e) { "Failed to serialize config: ${e.message}" }
                    }
                }

            is GatewayConfigLoadResult.Failure<*> -> schemas.mapValues { LoadedSchema(it.value, it.value.default) }
                .also {
                    logger.warn { "Failed to load config: ${loadResult.throwable.message}" }
                }
        }
    }

    override fun <T : Any> getConfig(key: String): T {
        val schema = loadedConfig[key] ?: throw NoSuchElementException("No config found for key: $key")
        val data = schema.data

        @Suppress("UNCHECKED_CAST") if (data::class == schema.schema.injectableType) return data as T

        throw IllegalArgumentException("Config data type does not match schema type for key: $key")
    }
}
