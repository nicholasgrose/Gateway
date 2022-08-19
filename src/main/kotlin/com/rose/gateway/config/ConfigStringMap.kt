package com.rose.gateway.config

import com.rose.gateway.config.markers.ConfigItem
import com.rose.gateway.config.markers.ConfigObject
import com.rose.gateway.config.schema.Config
import com.rose.gateway.shared.error.notNull
import com.rose.gateway.shared.reflection.asClass
import com.rose.gateway.shared.reflection.canBe
import org.koin.core.component.KoinComponent
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties
import kotlin.reflect.typeOf

/**
 * A map of strings to their respective config [Item]s.
 *
 * @constructor Creates a config string map.
 */
class ConfigStringMap : KoinComponent {
    val itemMap = mutableMapOf<String, Item<*>>()

    init {
        fillInItemMap(Config::class)
    }

    /**
     * Fills in the item map with all properties marked as [ConfigItem]s.
     *
     * @param config The config class to pull items from.
     * @param prefix The string path up to this config class.
     */
    private fun fillInItemMap(config: KClass<*>, prefix: String? = null) {
        for (property in config.memberProperties) {
            val configAnnotations = property.annotations.filterIsInstance<ConfigItem>()

            if (configAnnotations.isEmpty()) continue

            addPropertyToMap(property, prefix, configAnnotations.first())
        }
    }

    /**
     * Adds an item for the provided property to the item map.
     *
     * @param property The property to make an item for in the map.
     * @param prefix The string prefix, if any, to put in front of this property's path.
     * @param configAnnotation The config annotation attached to this property.
     */
    private fun addPropertyToMap(property: KProperty1<out Any, *>, prefix: String?, configAnnotation: ConfigItem) {
        val propertyString = if (prefix == null) property.name else "$prefix.${property.name}"
        val propertyType = property.returnType

        if (propertyType canBe typeOf<ConfigObject>()) {
            val propertyClass =
                propertyType.asClass().notNull("non-object member of Gateway schema marked as ConfigObject")

            fillInItemMap(propertyClass, propertyString)
        } else if (property is KMutableProperty1<*, *>) {
            itemMap[propertyString] = Item(property, propertyString, configAnnotation.description)
        }
    }

    /**
     * Gets an item from the item map whose path matches the given string.
     *
     * @param configString The config path to pull from the map.
     * @return The matched item or null, if none matched.
     */
    fun fromString(configString: String): Item<*>? {
        return itemMap[configString]
    }

    /**
     * All config strings.
     *
     * @return A list of all config strings in the item map.
     */
    fun allStrings(): List<String> = itemMap.keys.toList()
}
