package com.rose.gateway.shared.reflection

import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.isSubtypeOf

/**
 * Checks whether a class can be cast to another class.
 *
 * @param other The class we want to check if this class is a subclass of.
 * @return Whether this class is a subtype of the other class.
 */
infix fun KClass<*>.canBe(other: KClass<*>): Boolean = this.isSubclassOf(other)

/**
 * Checks whether a type can be cast to another type.
 *
 * @param other The type we want to check if this type is a subtype of.
 * @return Whether this type is a subtype of the other type.
 */
infix fun KType.canBe(other: KType): Boolean = this.isSubtypeOf(other)
