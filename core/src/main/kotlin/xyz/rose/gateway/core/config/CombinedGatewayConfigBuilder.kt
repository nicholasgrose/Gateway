package xyz.rose.gateway.core.config

/**
 * Builds a gateway config for a Gateway app embedded in the JVM
 *
 * @constructor Create an empty Gateway config builder
 */
class CombinedGatewayConfigBuilder(
    val file: GatewayConfigSource<CombinedGatewayConfig>
) : GatewayConfigBuilder {
    override val schemas = mutableListOf<GatewayConfigSchema<*>>()

    override fun build(): GatewayConfig = CombinedGatewayConfig(file, schemas.associateBy { it.key })
}
