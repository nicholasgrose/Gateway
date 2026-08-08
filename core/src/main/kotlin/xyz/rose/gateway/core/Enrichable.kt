package xyz.rose.gateway.core

/**
 * An object that can be enriched with data from other platforms.
 */
interface Enrichable {
    /**
     * A map of metadata for this object.
     * The key is a unique identifier for the enricher.
     */
    val metadata: MutableMap<String, EnricherMetadata>
}

/**
 * A marker interface for metadata that can be added to an [Enrichable] object.
 */
interface EnricherMetadata
