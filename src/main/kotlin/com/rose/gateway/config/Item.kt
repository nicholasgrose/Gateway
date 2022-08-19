package com.rose.gateway.config

import com.rose.gateway.config.markers.ConfigObject
import com.rose.gateway.shared.reflection.asClass
import com.rose.gateway.shared.reflection.canBe
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KType
import kotlin.reflect.full.memberProperties
import kotlin.reflect.typeOf

data class Item<T>(
    val property: KMutableProperty<T>,
    val path: String,
    val description: String
) : KoinComponent {
    private val config: PluginConfig by inject()

    fun type(): KType {
        return property.returnType
    }

    fun typeName(): String {
        val type = type()
        val simpleName = type.asClass()?.simpleName.toString()
        val suffix = if (type.arguments.isEmpty()) "" else type.arguments.joinToString(", ", "<", ">") {
            it.type?.asClass()?.simpleName.toString()
        }

        return "$simpleName$suffix"
    }

    fun get(): T {
        return property.getter.call(containingObject())
    }

    private fun containingObject(source: ConfigObject = config.config): ConfigObject? {
        val configProperties = source::class.memberProperties.filterConfigItems()

        for (member in configProperties) {
            val result =
                if (member == property) source
                else if (member.returnType canBe typeOf<ConfigObject>()) {
                    val memberValue = member.getter.call(source) as ConfigObject

                    containingObject(memberValue)
                } else null

            return result ?: continue
        }

        return null
    }

    fun set(newValue: T) {
        property.setter.call(containingObject(), newValue)
    }
}
