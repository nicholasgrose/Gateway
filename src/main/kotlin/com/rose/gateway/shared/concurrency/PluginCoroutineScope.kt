package com.rose.gateway.shared.concurrency

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.job
import kotlin.coroutines.CoroutineContext

/**
 * The coroutine scope for launching concurrent tasks throughout the plugin
 * This should only ever be instantiated once
 * If it is needed, it can be injected via Koin
 *
 * @constructor Create plugin coroutine scope
 */
class PluginCoroutineScope : CoroutineScope {
    override val coroutineContext: CoroutineContext
        // The supervisor job means that a failing job will not cause the plugin to fail.
        get() = SupervisorJob() + Dispatchers.Default

    /**
     * Cancels all active jobs in the coroutine context and then joins its job until it completes
     */
    suspend fun cancelAndJoinContext() {
        coroutineContext.job.cancelAndJoin()
    }
}
