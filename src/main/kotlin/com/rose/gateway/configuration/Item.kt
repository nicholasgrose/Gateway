package com.rose.gateway.configuration

import com.rose.gateway.shared.configurations.asClass
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KType

data class Item<T>(
    val property: KMutableProperty<T>,
    val description: String
) {
    fun type(): KType {
        return property.returnType
    }

    fun typeClass(): KClass<*> {
        return type().asClass()
    }

    fun typeName(): String {
        return typeClass().simpleName.toString()
    }

    fun get(): T {
        return property.getter.call()
    }

    fun set(newValue: T) {
        property.setter.call(newValue)
    }
}
