package com.rose.gateway.shared.concurrency

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

/**
 * The coroutine scope for launching concurrent tasks throughout the plugin.
 * This should only ever be instantiated once.
 * If it is needed, it can be injected via Koin.
 *
 * @constructor Create plugin coroutine scope.
 */
class PluginCoroutineScope : CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default
}
