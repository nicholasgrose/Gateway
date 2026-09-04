package xyz.rose.gateway.platform.minecraft.common.info

import xyz.rose.gateway.core.capability.info.connection.ConnectionInfo
import xyz.rose.gateway.platform.minecraft.common.config.MinecraftConfig

class MinecraftIpInfo(config: MinecraftConfig) {
    val host: String = config.address.host
    val port: Int = config.address.port
}
