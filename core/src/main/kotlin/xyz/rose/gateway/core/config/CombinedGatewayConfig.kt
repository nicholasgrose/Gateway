package xyz.rose.gateway.core.config

/**
 * The config for the embedded Gateway app
 *
 * @property schemas The config schemas
 * @constructor Create a new embedded Gateway config
 */
class CombinedGatewayConfig(
    val file: GatewayConfigSource<CombinedGatewayConfig>,
    val schemas: Map<String, GatewayConfigSchema<*>>
) : GatewayConfig {
    override fun <T : Any> getConfig(key: String): T {
        TODO("Not yet implemented")
    }
}
