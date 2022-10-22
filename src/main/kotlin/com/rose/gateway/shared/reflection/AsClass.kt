package com.rose.gateway.shared.reflection

import kotlin.reflect.KClass
import kotlin.reflect.KType

/**
 * Gets the classifier of a type as a KClass if it is a KClass
 *
 * @return KClass, if the type classifier is a KClass, or null, if it isn't
 */
fun KType.asClass(): KClass<*>? {
    val classifier = this.classifier

    return classifier as? KClass<*>
}
