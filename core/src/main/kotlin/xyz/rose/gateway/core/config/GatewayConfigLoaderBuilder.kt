package xyz.rose.gateway.core.config

interface GatewayConfigLoaderBuilder {
    fun register(provider: GatewayConfigProvider)
    fun build(): GatewayConfigLoader
}
