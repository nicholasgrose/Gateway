package xyz.rose.gateway.core

import xyz.rose.gateway.core.platform.PlatformType

/**
 * Data that can be enriched with additional information by a platform
 *
 * @property metadata A map of platform types to enricher metadata keyed by that platform's UID
 */
interface Enrichable {
    val metadata: Map<PlatformType, Map<String, EnricherMetadata>>
}

/**
 * Metadata for enriching a message with additional information
 */
interface EnricherMetadata
