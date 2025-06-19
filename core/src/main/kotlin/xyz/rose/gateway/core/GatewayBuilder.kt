package xyz.rose.gateway.core

import xyz.rose.gateway.core.config.GatewayConfigLoaderBuilder

interface GatewayBuilder {
    val configBuilder: GatewayConfigLoaderBuilder
    fun build(): GatewayApp
}
