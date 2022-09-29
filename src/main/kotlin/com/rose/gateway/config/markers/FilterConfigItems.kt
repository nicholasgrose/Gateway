package com.rose.gateway.config.markers

import kotlin.reflect.KProperty

/**
 * Filters a collection of KProperties by whether they are marked as [ConfigItem]s
 *
 * @return Those elements which are config items
 *
 * @see KProperty
 * @see ConfigItem
 */
fun Collection<KProperty<*>>.filterConfigItems(): List<KProperty<*>> {
    return this.filter { it.isConfigItem() }
}

/**
 * Determines whether the [KProperty] has the [ConfigItem] annotation
 *
 * @return Whether the property is a config item
 *
 * @see KProperty
 * @see ConfigItem
 */
fun KProperty<*>.isConfigItem(): Boolean {
    return this.annotations.any { it is ConfigItem }
}
