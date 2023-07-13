package com.rose.gateway.shared.concurrency

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * Conditionally launches a block of code
 *
 * @param condition The condition to check
 * @param block The block to launch if the condition is true
 * @receiver This [PluginCoroutineScope]
 * @return The job created for the launched code or null if the condition was false, meaning none was created
 *
 * @see PluginCoroutineScope
 */
fun PluginCoroutineScope.launchIf(condition: Boolean, block: suspend CoroutineScope.() -> Unit): Job? {
    return if (condition) {
        launch(block = block)
    } else {
        null
    }
}

/**
 * Launches some code in a blocking context if the condition is true
 *
 * @param T The type to return from running the block
 * @param condition The condition to check before running
 * @param block The block of code to run if the condition is true
 * @receiver The coroutine scope for general runBlocking calls
 * @return The type to return from the code block or null if the code did not run
 */
fun <T> runBlockingIf(condition: Boolean, block: suspend CoroutineScope.() -> T): T? {
    return if (condition) {
        runBlocking(block = block)
    } else {
        null
    }
}
