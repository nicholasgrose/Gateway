package com.rose.gateway.shared.concurrency

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * Conditionally launches a block of code.
 *
 * @param condition The condition to check.
 * @param block The block to launch if the condition is true.
 * @receiver This [PluginCoroutineScope].
 * @return The job created for the launched code or null if the condition was false, meaning none was created.
 *
 * @see PluginCoroutineScope
 */
fun PluginCoroutineScope.launchIf(condition: Boolean, block: suspend CoroutineScope.() -> Unit): Job? {
    return if (condition) {
        launch(block = block)
    } else null
}
