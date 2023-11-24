package com.rose.gateway.shared.reflection

import kotlin.reflect.KType

/**
 * Represents the type with a simplified name
 */
val KType.simpleName: String
    get() {
        val type = this
        val simpleName = type.asClass()?.simpleName.toString()
        val suffix =
            if (type.arguments.isEmpty()) {
                ""
            } else {
                type.arguments.joinToString(", ", "<", ">") {
                    it.type?.asClass()?.simpleName.toString()
                }
            }

        return "$simpleName$suffix"
    }
