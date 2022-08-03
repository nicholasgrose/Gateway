package com.rose.gateway.shared.configurations

import com.rose.gateway.configuration.markers.ConfigItem
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.KType
import kotlin.reflect.full.isSubclassOf

fun KType.asClass(): KClass<*>? {
    val classifier = this.classifier

    return classifier as? KClass<*>
}

infix fun KClass<*>?.canBe(other: KClass<*>): Boolean = this?.isSubclassOf(other) ?: false

fun Collection<KProperty<*>>.filterConfigItems(): List<KProperty<*>> {
    return this.filter { it.isConfigItem() }
}

private fun <V> KProperty<V>.isConfigItem(): Boolean {
    return this.annotations.any { it is ConfigItem }
}
