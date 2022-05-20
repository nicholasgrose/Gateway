package com.rose.gateway.shared.configurations

import com.rose.gateway.Logger
import com.rose.gateway.configuration.markers.ConfigItem
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.KType

fun KType.asClass(): KClass<*> {
    val classifier = this.classifier

    return if (classifier is KClass<*>) {
        classifier
    } else {
        Logger.logInfo("Attempted to convert non-class type to class.")
        Nothing::class
    }
}

infix fun KClass<*>.canBe(other: KClass<*>): Boolean = other.java.isAssignableFrom(this.java)

fun Collection<KProperty<*>>.filterConfigItems(): List<KProperty<*>> {
    return this.filter { it.isConfigItem() }
}

private fun <V> KProperty<V>.isConfigItem(): Boolean {
    return this.annotations.any { it is ConfigItem }
}
