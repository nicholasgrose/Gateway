package com.rose.gateway.config

import com.rose.gateway.config.markers.ConfigItem
import com.rose.gateway.config.markers.ConfigObject
import com.rose.gateway.config.schema.Config
import com.rose.gateway.shared.collections.trie.Trie
import org.koin.core.component.KoinComponent
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

class ConfigStringMap : KoinComponent {
    val itemMap = mutableMapOf<String, Item<*>>()
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

    fun allStrings(): List<String> = specificationTrie.toList()
}
