package xyz.rose.gateway.core.capability

import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.koin.core.component.KoinComponent
import xyz.rose.gateway.core.GatewayRegistry
import xyz.rose.gateway.core.platform.GatewayPlatformDefinition

private val logger = KotlinLogging.logger {}

/**
 * Orchestrates the lifecycle of a [GatewayMessage].
 *
 * The dispatcher is responsible for:
 * 1. Finding all [MessageEnricher]s from all platforms.
 * 2. Running all enrichers in parallel to enrich the message.
 * 3. Broadcasting the fully enriched message to all [MessageListener]s in parallel.
 */
class GatewayDispatcher(val registry: GatewayRegistry) : KoinComponent {
    /**
     * Grabs and applies metadata from all applicable enrichers to a message.
     *
     * @param message The message to enrich.
     * @return The fully enriched message.
     */
    suspend fun <T : GatewayMessageData> enrich(
        message: GatewayMessage<T>
    ): GatewayMessage<T> = coroutineScope {
        logger.debug { "Enriching message: ${message.id} from platform: ${message.sourcePlatformId}" }

        val applicableEnrichers = findApplicableEnrichers(message)
        val enrichmentResults = applicableEnrichers.map { (enricher, definition) ->
            async { enrichSafely(enricher, definition, message) }
        }.awaitAll().filterNotNull()

        val metadataMap = aggregateMetadata(enrichmentResults)
        message.copy(metadata = message.metadata + metadataMap)
    }

    /**
     * Finds all applicable [MessageEnricher]s for a given [GatewayMessage].
     *
     * @param T The type of the message data.
     * @param message The message to find enrichers for.
     * @return A list of pairs containing the applicable enrichers and their corresponding [GatewayPlatformDefinition].
     */
    private fun <T : GatewayMessageData> findApplicableEnrichers(
        message: GatewayMessage<T>
    ): List<Pair<MessageEnricher<T>, GatewayPlatformDefinition<*>>> =
        registry.definitionsByEnricher.mapNotNull { (capability, definition) ->
            if (capability.dataType.isInstance(message.data)) {
                @Suppress("UNCHECKED_CAST")
                (capability as MessageEnricher<T>) to definition
            } else {
                null
            }
        }

    /**
     * Attempts to enrich a [GatewayMessage] with a given [MessageEnricher].
     *
     * @param enricher The enricher to use.
     * @param definition The platform definition for the enricher.
     * @param message The message to enrich.
     * @return A pair containing the platform definition and the enriched metadata, or null if enrichment fails.
     */
    private suspend fun <T : GatewayMessageData> enrichSafely(
        enricher: MessageEnricher<T>,
        definition: GatewayPlatformDefinition<*>,
        message: GatewayMessage<T>
    ): Pair<GatewayPlatformDefinition<*>, MessageMetadata>? =
        runCatching {
            val metadata = enricher.enrich(message)
            if (metadata != null) definition to metadata else null
        }.onFailure {
            logger.error(it) {
                "Failed to enrich message ${message.id} with enricher: ${enricher::class.simpleName}"
            }
        }.getOrThrow()

    /**
     * Aggregates the metadata from a list of enricher results into a map that matches the [MessageMetadataMap] format.
     *
     * @param results The list of enricher results to aggregate.
     * @return A map containing the aggregated metadata.
     */
    private fun aggregateMetadata(
        results: List<Pair<GatewayPlatformDefinition<*>, MessageMetadata>>
    ): MessageMetadataMap =
        results
            .groupBy { (definition, _) -> definition.type }
            .mapValues { (_, platformGroup) ->
                platformGroup.groupBy { (definition, _) -> definition.uid }
                    .mapValues { (_, entries) -> entries.map { (_, metadata) -> metadata } }
            }

    /**
     * Broadcasts a message to all matching [MessageListener]s in parallel.
     *
     * @param message The message to broadcast.
     * @return A list of results from the listeners.
     */
    suspend fun <T : GatewayMessageData> broadcast(
        message: GatewayMessage<T>
    ) = coroutineScope {
        logger.debug { "Broadcasting message: ${message.id}" }

        val listeners = findApplicableListeners(message)
        listeners.map { listener ->
            async { broadcastSafely(listener, message) }
        }.awaitAll()
    }

    /**
     * Finds all applicable [MessageListener]s for a given message.
     *
     * @param message The message to find listeners for.
     * @return A list of applicable listeners.
     */
    fun <T : GatewayMessageData> findApplicableListeners(
        message: GatewayMessage<T>
    ): List<MessageListener<T>> =
        registry.definitionsByListener.mapNotNull { (capability, _) ->
            if (capability.dataType.isInstance(message.data)) {
                @Suppress("UNCHECKED_CAST")
                capability as MessageListener<T>
            } else {
                null
            }
        }

    /**
     * Broadcasts a message to a listener safely.
     *
     * @param T The type of the message.
     * @param listener The listener to broadcast to.
     * @param message The message to broadcast.
     */
    private suspend fun <T : GatewayMessageData> broadcastSafely(
        listener: MessageListener<T>,
        message: GatewayMessage<T>
    ) =
        runCatching {
            listener.onMessage(message)
        }.onFailure {
            logger.error(it) {
                "Failed to broadcast message ${message.id} to listener: ${listener::class.simpleName}"
            }
        }.getOrThrow()

    /**
     * Dispatches a message for platform-shared enrichment and broadcasting.
     *
     * @param T The type of the message.
     * @param message The message to dispatch.
     */
    suspend fun <T : GatewayMessageData> dispatch(message: GatewayMessage<T>) {
        val enriched = enrich(message)
        broadcast(enriched)
    }
}
