package xyz.rose.gateway.platform.minecraft.common.info

import xyz.rose.gateway.core.capability.info.OnlineStats

class MinecraftOnlinePlayers(players: OnlinePlayers) : OnlineStats {
    override val online: List<String> = players.players

    interface OnlinePlayers {
        val players: List<String>
    }
}
