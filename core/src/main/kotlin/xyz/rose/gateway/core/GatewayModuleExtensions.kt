package xyz.rose.gateway.core

import org.koin.core.definition.KoinDefinition
import org.koin.dsl.binds
import xyz.rose.gateway.core.capability.Capability
import xyz.rose.gateway.core.capability.MessageEnricher
import xyz.rose.gateway.core.capability.MessageListener
import xyz.rose.gateway.core.platform.Platform
import xyz.rose.gateway.core.plugin.GatewayPlugin

/**
 * A convenience extension to bind a platform definition to the [Platform] interface.
 *
 * @param T The type of platform to bind.
 * @return The bound [KoinDefinition]
 */
inline fun <reified T : Platform> KoinDefinition<T>.bindPlatform() =
    this binds arrayOf(Platform::class, T::class)

/**
 * A convenience extension to bind a platform definition to the [GatewayPlugin] interface.
 *
 * @param T The type of plugin to bind.
 * @return The bound [KoinDefinition]
 */
inline fun <reified T : GatewayPlugin> KoinDefinition<T>.bindPlugin() =
    this binds arrayOf(GatewayPlugin::class, T::class)

/**
 * A convenience extension to bind a capability definition to the [Capability] interface.
 *
 * @param T The type of capability to bind.
 * @return The bound [KoinDefinition]
 */
inline fun <reified T : Capability> KoinDefinition<T>.bindCapability() =
    this binds arrayOf(Capability::class, T::class)

/**
 * A convenience extension to bind an enricher definition to the [MessageEnricher] interface.
 *
 * @param T The type of enricher to bind.
 * @return The bound [KoinDefinition]
 */
inline fun <reified T : MessageEnricher<*>> KoinDefinition<T>.bindEnricher() =
    this binds arrayOf(Capability::class, MessageEnricher::class, T::class)

/**
 * A convenience extension to bind a listener definition to the [MessageListener] interface.
 *
 * @param T The type of listener to bind.
 * @return The bound [KoinDefinition]
 */
inline fun <reified T : MessageListener<*, *>> KoinDefinition<T>.bindListener() =
    this binds arrayOf(Capability::class, MessageListener::class, T::class)
