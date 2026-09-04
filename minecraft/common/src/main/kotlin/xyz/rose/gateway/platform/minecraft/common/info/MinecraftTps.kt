package xyz.rose.gateway.platform.minecraft.common.info

import xyz.rose.gateway.core.capability.info.performance.PerformanceStats

class MinecraftTps(val provider: TpsProvider){
    interface TpsProvider {
        fun getTps(): ServerTps
    }

    data class ServerTps(
        val oneMinute: Double,
        val fiveMinutes: Double,
        val fifteenMinutes: Double
    )
}
