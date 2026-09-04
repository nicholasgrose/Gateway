package xyz.rose.gateway.core

import org.koin.core.module.dsl.scopedOf
import org.koin.core.qualifier.Qualifier
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope
import org.koin.dsl.module
import org.koin.mp.KoinPlatform.getKoin
import xyz.rose.gateway.core.capability.Capability
import xyz.rose.gateway.core.capability.MessageEnricher
import xyz.rose.gateway.core.capability.MessageListener
import xyz.rose.gateway.core.capability.allowlist.AllowlistRead
import xyz.rose.gateway.core.capability.allowlist.AllowlistWrite
import xyz.rose.gateway.core.capability.chat.ChatSend
import xyz.rose.gateway.core.capability.info.connection.ConnectionInfo
import xyz.rose.gateway.core.capability.info.online.OnlineStats
import xyz.rose.gateway.core.capability.info.performance.PerformanceStats
import xyz.rose.gateway.core.capability.info.version.VersionQuery
import xyz.rose.gateway.core.platform.GatewayPlatformDefinition
import xyz.rose.gateway.core.platform.Platform
import xyz.rose.gateway.core.plugin.GatewayPlugin

/**
 * Manages the runtime instances of platforms and their associated plugins and capabilities.
 *
 * @property definitions The collection of platform definitions.
 */
class GatewayRegistry(
    private val definitions: Collection<GatewayPlatformDefinition<*>>
) {
    /**
     * The runtime instances of platforms and their associated plugins and capabilities.
     */
    val runtimes: Map<GatewayPlatformDefinition<*>, PlatformRuntime>

    /**
     * Platform definitions indexed by capability.
     */
    val definitionsByCapability: Map<Capability, GatewayPlatformDefinition<*>>

    /**
     * Platform definitions indexed by enricher.
     */
    val definitionsByEnricher: Map<MessageEnricher<*>, GatewayPlatformDefinition<*>>

    /**
     * Platform definitions indexed by message listener.
     */
    val definitionsByListener: Map<MessageListener<*>, GatewayPlatformDefinition<*>>

    /**
     * Platform definitions indexed by platform.
     */
    val definitionsByPlatform: Map<Platform, GatewayPlatformDefinition<*>>

    init {
        loadRuntimeModules()
        runtimes = fetchRuntimes()
        definitionsByCapability =
            runtimes.flatMap { it.value.capabilities.map { capability -> capability to it.key } }.toMap()
        definitionsByPlatform = runtimes.map { it.value.platform to it.key }.toMap()
        definitionsByEnricher = runtimes.flatMap { it.value.enrichers.map { enricher -> enricher to it.key } }.toMap()
        definitionsByListener = runtimes.flatMap { it.value.listeners.map { listener -> listener to it.key } }.toMap()
    }

    /**
     * Loads the runtime modules for each platform definition.
     */
    private fun loadRuntimeModules() {
        val modules = definitions.map { definition ->
            module {
                scope(scopeQualifier(definition)) {
                    scoped { definition }

                    // The platform gateway client service
                    scopedOf(::PlatformGatewayClient)

                    // Core app services
                    scopedOf(::VersionQuery)
                    scopedOf(::ConnectionInfo)
                    scopedOf(::OnlineStats)
                    scopedOf(::PerformanceStats)
                    scopedOf(::AllowlistRead)
                    scopedOf(::AllowlistWrite)
                    scopedOf(::ChatSend)

                    with(definition.provider) {
                        createRuntimeModule()
                    }
                }
            }
        }

        getKoin().loadModules(modules)
    }

    /**
     * Constructs the runtime instances for each platform definition.
     * @return The runtime instances for each platform definition.
     */
    private fun fetchRuntimes(): Map<GatewayPlatformDefinition<*>, PlatformRuntime> {
        return definitions.associateWith { definition ->
            val scope = getKoin().getOrCreateScope(scopeId(definition), scopeQualifier(definition))

            PlatformRuntime(
                definition = definition,
                scope = scope,
                platform = scope.get(),
                capabilities = scope.getAll(),
                enrichers = scope.getAll(),
                listeners = scope.getAll(),
                plugins = scope.getAll()
            )
        }
    }

    /**
     * A runtime for a platform definition.
     *
     * @property definition The platform definition.
     * @property scope The Koin scope for the runtime.
     * @property platform The platform instance.
     * @property capabilities The capabilities provided by the platform.
     * @property enrichers The message enrichers associated with the platform.
     * @property listeners The message listeners associated with the platform.
     * @property plugins The plugins associated with the platform.
     */
    data class PlatformRuntime(
        val definition: GatewayPlatformDefinition<*>,
        val scope: Scope,
        val platform: Platform,
        val capabilities: List<Capability>,
        val enrichers: List<MessageEnricher<*>>,
        val listeners: List<MessageListener<*>>,
        val plugins: List<GatewayPlugin>
    )

    /**
     * Gets the qualifier for a Koin scope.
     *
     * @param definition The platform definition.
     * @return The qualifier for the Koin scope.
     */
    private fun scopeQualifier(definition: GatewayPlatformDefinition<*>): Qualifier {
        return named(scopeId(definition))
    }

    /**
     * Gets the ID for a Koin scope.
     *
     * @param definition The platform definition.
     * @return The ID for the Koin scope.
     */
    private fun scopeId(definition: GatewayPlatformDefinition<*>): String {
        return "${definition.type}_${definition.uid}"
    }

    /**
     * Closes all Koin scopes associated with platform runtimes.
     */
    fun close() {
        runtimes.forEach { it.value.scope.close() }
    }
}
