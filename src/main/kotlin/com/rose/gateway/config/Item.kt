package com.rose.gateway.config

import com.rose.gateway.config.markers.ConfigObject
import com.rose.gateway.config.markers.filterConfigItems
import com.rose.gateway.shared.error.notNull
import com.rose.gateway.shared.reflection.canBe
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KType
import kotlin.reflect.full.memberProperties
import kotlin.reflect.typeOf

/**
 * A single item of the config
 *
 * @param ValueType The type of the value contained in this config item
 * @property property The property of the config class that this item references
 * @property path The path of this item in the config file represented as a dot-delimited string
 * @property description The description of this item
 * @constructor Create an item with the provided data
 */
data class Item<ValueType>(
    val property: KMutableProperty<ValueType>,
    val path: String,
    val description: String
) : KoinComponent {
    private val pluginConfig: PluginConfig by inject()

    val type: KType = property.returnType
    var value: ValueType
        get() = property.getter.call(containingObject())
        set(value) = property.setter.call(containingObject(), value)

    /**
     * Finds the [ConfigObject] that contains this item's property
     *
     * @return The object found
     */
    private fun containingObject(): ConfigObject {
        return containingObject(pluginConfig.config)
            .notNull("no ConfigObject exists that contains referenced item: $this")
    }

    /**
     * Finds the [ConfigObject] that contains this item's property using a depth-first search
     *
     * @param source The object to search
     * @return The object found, if any
     */
    private fun containingObject(source: ConfigObject): ConfigObject? {
        val configProperties = source::class.memberProperties.filterConfigItems()

        for (member in configProperties) {
            val result = if (member == property) source
            else if (member.returnType canBe typeOf<ConfigObject>()) {
                val memberValue = member.getter.call(source) as ConfigObject

                containingObject(memberValue)
            } else null

            return result ?: continue
        }

        return null
    }
}
