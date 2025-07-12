package xyz.rose.gateway.core.config

import kotlinx.serialization.KSerializer

interface GatewayConfigLoaderBuilder {
    val serializers: Map<String, KSerializer<*>>

    fun build(): GatewayConfigLoader
}
