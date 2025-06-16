package xyz.rose.gateway.core.core.platform

interface GatewayPlatformBuilder<T : GatewayPlatform> {
    fun build(): T
}
