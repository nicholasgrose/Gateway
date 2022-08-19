package com.rose.gateway.shared.concurrency

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

class PluginCoroutineScope : CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default
}
