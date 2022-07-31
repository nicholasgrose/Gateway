package com.rose.gateway.configuration

import com.rose.gateway.configuration.markers.ConfigObject
import com.rose.gateway.shared.configurations.asClass
import com.rose.gateway.shared.configurations.canBe
import com.rose.gateway.shared.configurations.filterConfigItems
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KType
import kotlin.reflect.full.memberProperties

data class Item<T>(
    val property: KMutableProperty<T>,
    val path: String,
    val description: String
) : KoinComponent {
    private val config: PluginConfiguration by inject()

    fun type(): KType {
        return property.returnType
    }

    fun typeName(): String {
        return type().toString()
    }

    fun get(): T {
        return property.getter.call(containingObject())
    }

    private fun containingObject(source: ConfigObject = config.config): ConfigObject? {
        val configProperties = source::class.memberProperties.filterConfigItems()

        for (member in configProperties) {
            val result =
                if (member == property) source
                else if (member.returnType.asClass() canBe ConfigObject::class) {
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
