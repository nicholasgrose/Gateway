package xyz.rose.gateway.platform.minecraft.common.info

import xyz.rose.gateway.core.capability.info.version.VersionRequestData

class MinecraftVersionInfo(versionProvider: VersionProvider) {    interface VersionProvider {
        val version: String
    }
}
