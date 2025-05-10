package xyz.rose.gateway.minecraft.capability

import xyz.rose.gateway.capability.PerformanceReadCapability

/**
 * Interface for capabilities that provide read access to Minecraft TPS (Ticks Per Second) metrics.
 * This capability allows reading TPS information from a Minecraft server.
 */
interface MinecraftTpsReadCapability : PerformanceReadCapability {
    /**
     * Gets the current TPS (Ticks Per Second) of the server.
     *
     * @return The current TPS.
     */
    override fun getCurrentPerformance(): Double

    /**
     * Gets the average TPS over the last minute.
     *
     * @return The average TPS over the last minute.
     */
    override fun getAveragePerformance1Min(): Double

    /**
     * Gets the average TPS over the last 5 minutes.
     *
     * @return The average TPS over the last 5 minutes.
     */
    override fun getAveragePerformance5Min(): Double

    /**
     * Gets the average TPS over the last 15 minutes.
     *
     * @return The average TPS over the last 15 minutes.
     */
    override fun getAveragePerformance15Min(): Double
}
