package com.rose.gateway.config

import com.rose.gateway.config.markers.ConfigItem
import kotlin.reflect.KProperty

fun Collection<KProperty<*>>.filterConfigItems(): List<KProperty<*>> {
    return this.filter { it.isConfigItem() }
}

private fun <V> KProperty<V>.isConfigItem(): Boolean {
    return this.annotations.any { it is ConfigItem }
}
