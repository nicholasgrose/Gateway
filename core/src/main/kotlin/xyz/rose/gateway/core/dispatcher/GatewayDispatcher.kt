package xyz.rose.gateway.core.dispatcher

import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.koin.core.component.KoinComponent
import xyz.rose.gateway.core.capability.message.GatewayMessage
import xyz.rose.gateway.core.capability.message.MessageEnricher
import xyz.rose.gateway.core.capability.message.MessageListener
import xyz.rose.gateway.core.platform.Platform

private val logger = KotlinLogging.logger {}

/**
 * Orchestrates the lifecycle of a [GatewayMessage].
 *
 * The dispatcher is responsible for:
 * 1. Finding all [MessageEnricher]s from all platforms.
 * 2. Running all enrichers in parallel to enrich the message.
 * 3. Broadcasting the fully enriched message to all [MessageListener]s in parallel.
 */
class GatewayDispatcher : KoinComponent {
    /**
     * Dispatches a message through the enrichment and broadcasting pipeline.
     *
     * @param message The message to dispatch
     */
    suspend fun dispatch(message: GatewayMessage) = coroutineScope {
        logger.debug { "Dispatching message: ${message.id} from platform: ${message.sourcePlatformId}" }

        // 1. Get all platforms
        val platforms = getKoin().getAll<Platform>()
        
        // 2. Parallel enrichment
        val enrichers = platforms.flatMap { it.capabilities }.filterIsInstance<MessageEnricher>()
        logger.trace { "Found ${enrichers.size} enrichers for message: ${message.id}" }
        
        enrichers.map { enricher ->
            async {
                runCatching { enricher.enrich(message) }
                    .onFailure { e -> logger.error(e) { "Failed to enrich message ${message.id} with enricher: ${enricher::class.simpleName}" } }
            }
        }.awaitAll()

        // 3. Parallel broadcasting
        val listeners = platforms.flatMap { it.capabilities }.filterIsInstance<MessageListener>()
        logger.trace { "Found ${listeners.size} listeners for message: ${message.id}" }

        listeners.map { listener ->
            async {
                runCatching { listener.onMessage(message) }
                    .onFailure { e -> logger.error(e) { "Failed to broadcast message ${message.id} to listener: ${listener::class.simpleName}" } }
            }
        }.awaitAll()
        
        logger.debug { "Finished dispatching message: ${message.id}" }
    }
}
