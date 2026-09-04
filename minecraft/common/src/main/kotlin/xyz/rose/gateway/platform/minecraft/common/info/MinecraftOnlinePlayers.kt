package xyz.rose.gateway.platform.minecraft.common.info

import xyz.rose.gateway.core.capability.info.online.OnlineStats

class MinecraftOnlinePlayers(players: OnlinePlayers) {
    interface OnlinePlayers {
        val players: List<String>
    }
}
