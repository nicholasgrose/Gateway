package xyz.rose.gateway.capability

/**
 * Interface for capabilities that provide read access to performance metrics.
 * This capability allows reading performance information from a platform.
 */
interface PerformanceReadCapability : Capability {
    /**
     * Gets the current performance metric.
     *
     * @return The current performance metric as a double.
     */
    fun getCurrentPerformance(): Double

    /**
     * Gets the average performance over the last minute.
     *
     * @return The average performance over the last minute as a double.
     */
    fun getAveragePerformance1Min(): Double

    /**
     * Gets the average performance over the last 5 minutes.
     *
     * @return The average performance over the last 5 minutes as a double.
     */
    fun getAveragePerformance5Min(): Double

    /**
     * Gets the average performance over the last 15 minutes.
     *
     * @return The average performance over the last 15 minutes as a double.
     */
    fun getAveragePerformance15Min(): Double
}
