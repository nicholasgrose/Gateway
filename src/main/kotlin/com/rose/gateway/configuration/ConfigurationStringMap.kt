package com.rose.gateway.configuration

import com.rose.gateway.shared.collections.trie.Trie
import com.uchuhimo.konf.Item
import com.uchuhimo.konf.Spec

class ConfigurationStringMap(baseSpecification: Spec) {
    private val specificationMap = mutableMapOf<String, Item<*>>()
    private val specificationTrie = Trie()

    init {
        fillInSpecificationMap(baseSpecification)
        specificationTrie.addAll(specificationMap.keys)
    }

    private fun fillInSpecificationMap(pluginSpec: Spec, prefix: String = pluginSpec.prefix) {
        for (item in pluginSpec.items) {
            specificationMap["$prefix.${item.name}"] = item
        }

        for (innerSpec in pluginSpec.innerSpecs) {
            fillInSpecificationMap(innerSpec, "$prefix.${innerSpec.prefix}")
        }
    }

    fun specificationFromString(configurationString: String): Item<*>? {
        return specificationMap[configurationString]
    }

    fun matchingOrAllConfigurationStrings(configurationString: String): List<String> {
        return specificationTrie.searchOrGetAll(configurationString)
    }
}
