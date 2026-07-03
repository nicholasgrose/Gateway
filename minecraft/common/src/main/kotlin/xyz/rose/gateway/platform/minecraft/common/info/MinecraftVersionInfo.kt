package xyz.rose.gateway.platform.minecraft.common.info

import xyz.rose.gateway.core.capability.info.VersionInfo

class MinecraftVersionInfo(versionProvider: VersionProvider) : VersionInfo {
    override val version: String = versionProvider.version

    interface VersionProvider {
        val version: String
    }
}
