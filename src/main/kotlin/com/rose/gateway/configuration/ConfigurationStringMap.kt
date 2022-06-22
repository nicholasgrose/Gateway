package com.rose.gateway.configuration

import com.rose.gateway.configuration.markers.ConfigItem
import com.rose.gateway.configuration.markers.ConfigObject
import com.rose.gateway.configuration.schema.Config
import com.rose.gateway.shared.collections.trie.Trie
import com.rose.gateway.shared.configurations.canBe
import org.koin.core.component.KoinComponent
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

class ConfigurationStringMap : KoinComponent {
    private val itemMap = mutableMapOf<String, Item<*>>()
    private val specificationTrie = Trie()

    init {
        fillInItemMap(Config::class)
        specificationTrie.addAll(itemMap.keys)
    }

    private fun fillInItemMap(config: KClass<*>, prefix: String? = null) {
        for (property in config.memberProperties) {
            val configAnnotations = property.annotations.filterIsInstance<ConfigItem>()

            if (configAnnotations.isEmpty()) continue

            addPropertyToMap(property, prefix, configAnnotations.first())
        }
    }

    private fun addPropertyToMap(property: KProperty1<out Any, *>, prefix: String?, configAnnotation: ConfigItem) {
        val propertyString = if (prefix == null) property.name else "$prefix.${property.name}"
        val classifier = property.returnType.classifier

        if (classifier != null &&
            classifier is KClass<*> &&
            classifier canBe ConfigObject::class
        ) {
            fillInItemMap(classifier, propertyString)
        } else if (property is KMutableProperty1<*, *>) {
            itemMap[propertyString] = Item(property, propertyString, configAnnotation.description)
        }
    }

    fun fromString(configurationString: String): Item<*>? {
        return itemMap[configurationString]
    }

    fun matchingOrAllStrings(configurationString: String): List<String> {
        return specificationTrie.searchOrGetAll(configurationString)
    }

    fun allStrings(): List<String> = specificationTrie.toList()
}
