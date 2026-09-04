package xyz.rose.gateway.platform.minecraft.common.whitelist

import xyz.rose.gateway.core.capability.allowlist.AllowlistRead
import xyz.rose.gateway.core.capability.allowlist.AllowlistWrite

class MinecraftWhitelist(val accessor: MinecraftWhitelistAccessor) {
    interface MinecraftWhitelistAccessor {
        fun getWhitelist(): List<String>
        fun addPlayer(name: String): Boolean
        fun removePlayer(name: String): Boolean
    }
}
