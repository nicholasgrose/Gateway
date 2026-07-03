package xyz.rose.gateway.platform.minecraft.common.info

import xyz.rose.gateway.core.capability.info.PerformanceStats

class MinecraftTps(val provider: TpsProvider) : PerformanceStats {
    override val statName = ""

    override val lastMeasurement: Map<String, PerformanceStats.Measurement>
        get() {
            val serverTps = provider.getTps()

            return mapOf(
                "1m" to tpsMeasurement(serverTps.oneMinute),
                "5m" to tpsMeasurement(serverTps.fiveMinutes),
                "15m" to tpsMeasurement(serverTps.fifteenMinutes),
            )
        }

    fun tpsMeasurement(amount: Double): PerformanceStats.Measurement {
        return PerformanceStats.Measurement(
            amount,
            "t/s"
        )
    }

    interface TpsProvider {
        fun getTps(): ServerTps
    }

    data class ServerTps(
        val oneMinute: Double,
        val fiveMinutes: Double,
        val fifteenMinutes: Double
    )
}
