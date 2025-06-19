package xyz.rose.gateway.core.platform

interface GatewayPlatformBuilder<T : GatewayPlatform> {
    fun build(): T
}
