package xyz.rose.gateway.platform.minecraft.common.info

import xyz.rose.gateway.core.capability.info.ConnectionInfo
import xyz.rose.gateway.platform.minecraft.common.config.MinecraftConfig

class MinecraftIpInfo(config: MinecraftConfig) : ConnectionInfo {
    override val host: String = config.address.host
    override val port: Int = config.address.port
}
