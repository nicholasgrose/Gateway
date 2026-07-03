package xyz.rose.gateway.core.capability.info

import xyz.rose.gateway.core.capability.Capability

/**
 * Gateway capability for performance metrics
 */
interface PerformanceStats : Capability {
    /**
     * The name of the performance stat being measured
     */
    val statName: String

    /**
     * The last measured stats, providing a list of measurements keyed by time
     */
    val lastMeasurement: Map<String, Measurement>

    /**
     * A measurement of performance in the units the platform uses
     */
    data class Measurement(val amount: Double, val unit: String)
}
