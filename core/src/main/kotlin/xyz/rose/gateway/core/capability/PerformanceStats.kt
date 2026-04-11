package xyz.rose.gateway.core.capability

/**
 * Gateway capability for performance metrics
 */
interface PerformanceStats : GatewayCapability {
    /**
     * The last measurement
     */
    val lastMeasurement: Measurement

    /**
     * A measurement of performance in the units the platform uses
     */
    data class Measurement(val amount: Int, val unit: String)
}
