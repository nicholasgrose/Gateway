package xyz.rose.gateway.core

import org.koin.core.definition.KoinDefinition
import org.koin.dsl.binds
import xyz.rose.gateway.core.capability.GatewayCapability
import xyz.rose.gateway.core.platform.GatewayPlatform
import xyz.rose.gateway.core.plugin.GatewayPlugin

/**
 * A convenience extension to bind a platform definition to the [GatewayPlatform] interface.
 *
 * @param T The type of platform to bind.
 * @return The bound [KoinDefinition]
 */
inline fun <reified T : GatewayPlatform> KoinDefinition<T>.bindPlatform() =
    this binds arrayOf(GatewayPlatform::class, T::class)

/**
 * A convenience extension to bind a platform definition to the [GatewayPlugin] interface.
 *
 * @param T The type of plugin to bind.
 * @return The bound [KoinDefinition]
 */
inline fun <reified T : GatewayPlugin> KoinDefinition<T>.bindPlugin() =
    this binds arrayOf(GatewayPlugin::class, T::class)

/**
 * A convenience extension to bind a capability definition to the [GatewayCapability] interface.
 *
 * @param T The type of capability to bind.
 * @return The bound [KoinDefinition]
 */
inline fun <reified T : GatewayCapability> KoinDefinition<T>.bindCapability() =
    this binds arrayOf(GatewayCapability::class, T::class)
