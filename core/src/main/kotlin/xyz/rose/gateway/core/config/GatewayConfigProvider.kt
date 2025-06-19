package xyz.rose.gateway.core.config

interface GatewayConfigProvider {
    fun build(): GatewayConfig
}
