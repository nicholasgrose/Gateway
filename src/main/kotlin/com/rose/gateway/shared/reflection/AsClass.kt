package com.rose.gateway.shared.reflection

import kotlin.reflect.KClass
import kotlin.reflect.KType

/**
 * As class
 *
 * @return
 */
fun KType.asClass(): KClass<*>? {
    val classifier = this.classifier

    return classifier as? KClass<*>
}
