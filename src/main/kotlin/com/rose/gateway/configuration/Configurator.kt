package com.rose.gateway.configuration

import com.kotlindiscord.kord.extensions.extensions.Extension
import com.rose.gateway.GatewayPlugin
import com.rose.gateway.Logger
import com.rose.gateway.bot.extensions.about.AboutExtension
import com.rose.gateway.bot.extensions.chat.ChatExtension
import com.rose.gateway.bot.extensions.list.ListExtension
import com.rose.gateway.bot.extensions.whitelist.WhitelistExtension
import com.rose.gateway.minecraft.server.Scheduler
import com.rose.gateway.shared.trie.Trie
import com.uchuhimo.konf.Config
import com.uchuhimo.konf.Item
import com.uchuhimo.konf.Spec
import com.uchuhimo.konf.UnsetValueException
import com.uchuhimo.konf.source.yaml
import org.bukkit.Bukkit
import kotlin.reflect.KFunction

object Configurator {
    private val pluginSpecificationMap = buildSpecificationMap(PluginSpec)
    private val configurationTrie = buildConfigurationTrie()

    sealed class SpecificationMap {
        class InnerSpecification(val specification: Map<String, SpecificationMap>) : SpecificationMap()
        class SpecificationItem(val item: Item<*>) : SpecificationMap()
    }

    private fun buildSpecificationMap(
        specification: Spec,
        wrapSpecification: Boolean = true
    ): Map<String, SpecificationMap> {
        val result = mutableMapOf<String, SpecificationMap>()

        for (item in specification.items) {
            result[item.name] = SpecificationMap.SpecificationItem(item)
        }

        for (innerSpecification in specification.innerSpecs) {
            result[innerSpecification.prefix] =
                SpecificationMap.InnerSpecification(buildSpecificationMap(innerSpecification, false))
        }

        return if (wrapSpecification) {
            mapOf(specification.prefix to SpecificationMap.InnerSpecification(result))
        } else result
    }

    private fun buildConfigurationTrie(): Trie {
        val configurationTrie = Trie()

        val configurations = convertSpecificationMapToStrings(pluginSpecificationMap)
        configurationTrie.addAll(configurations)

        return configurationTrie
    }

    private fun convertSpecificationMapToStrings(specificationMap: Map<String, SpecificationMap>): List<String> {
        val result = mutableListOf<String>()

        for ((prefix, map) in specificationMap) {
            val mapStrings = when (map) {
                is SpecificationMap.InnerSpecification -> convertSpecificationMapToStrings(map.specification).map { config -> "$prefix.$config" }
                is SpecificationMap.SpecificationItem -> listOf(prefix)
            }
            result.addAll(mapStrings)
        }

        return result
    }

    fun searchForMatchingConfigurations(configuration: String): List<String> {
        return configurationTrie.searchForElementsWithPrefix(configuration)
    }

    fun getConfigurationInformation(configurationPath: String): Item<*>? {
        val configurationPathParts = configurationPath.split('.')
        var currentMap = pluginSpecificationMap
        var item: Item<*>? = null

        for ((index, configuration) in configurationPathParts.withIndex()) {
            val result = currentMap[configuration] ?: return null

            when (result) {
                is SpecificationMap.InnerSpecification -> {
                    currentMap = result.specification
                }
                is SpecificationMap.SpecificationItem -> {
                    if (index == configurationPathParts.size - 1) item = result.item
                    else break
                }
            }
        }

        return item
    }

    val config by lazy {
        runCatching {
            Config { addSpec(PluginSpec) }
                .from.yaml.watchFile("plugins/Gateway/Gateway.yaml")
        }.onFailure { error ->
            giveConfigErrorReport(
                error as Exception,
                "An error occurred while loading configuration! Please fix before starting again!"
            )
        }.getOrNull() ?: Config()
    }

    fun ensureProperConfiguration(): Boolean {
        return try {
            config.validateRequired()
            true
        } catch (error: UnsetValueException) {
            giveConfigErrorReport(error, "Configurations not set correctly! Please fix before starting again!")
            false
        }
    }

    private fun giveConfigErrorReport(error: Exception, additionalMessage: String) {
        Logger.log(additionalMessage)
        Logger.log(error.message ?: "No error message not found.")
        Scheduler.runTask {
            Bukkit.getServer().pluginManager.disablePlugin(GatewayPlugin.plugin)
        }
    }

    private val extensionSpecs = mapOf(
        ::WhitelistExtension to PluginSpec.BotSpec.EnabledExtensionsSpec.whitelistExtension,
        ::ListExtension to PluginSpec.BotSpec.EnabledExtensionsSpec.listExtension,
        ::AboutExtension to PluginSpec.BotSpec.EnabledExtensionsSpec.aboutExtension,
        ::ChatExtension to PluginSpec.BotSpec.EnabledExtensionsSpec.chatExtension
    )

    fun extensionEnabled(extension: KFunction<Extension>): Boolean {
        val spec = extensionSpecs[extension] ?: return false

        return config[spec]
    }
}
