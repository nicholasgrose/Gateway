package com.rose.gateway.config

import com.charleskorn.kaml.Yaml
import com.rose.gateway.GatewayPlugin
import com.rose.gateway.config.markers.CommonDecoder
import com.rose.gateway.config.schema.Config
import com.rose.gateway.minecraft.logging.Logger
import com.rose.gateway.shared.error.notNull
import com.sksamuel.hoplite.ConfigException
import com.sksamuel.hoplite.ConfigLoaderBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.nio.file.Files
import java.nio.file.Path

class GatewayConfigLoader : KoinComponent {
    private val plugin: GatewayPlugin by inject()

    companion object {
        const val DEFAULT_CONFIG_FILE_RESOURCE_NAME = "default_gateway_config.yaml"
        const val CONFIG_FILE_NAME = "config.yaml"
    }

    private val pluginDirPath = plugin.dataFolder.path.replace("\\", "/")
    private val configPath = Path.of("$pluginDirPath/$CONFIG_FILE_NAME")

    suspend fun loadOrCreateConfig(): Config {
        ensureConfigurationFileExists(configPath)

        val configuration = loadConfig(configPath.toString())

        if (configuration != null) {
            return configuration
        }

        return loadConfig(DEFAULT_CONFIG_FILE_RESOURCE_NAME).defaultConfigLoaded()
    }

    private fun <T> T?.defaultConfigLoaded(): T {
        return this.notNull("default config resource does not exist to be loaded")
    }

    private suspend fun ensureConfigurationFileExists(configurationFilePath: Path) {
        Logger.info("Checking configuration file existence...")

        if (!Files.exists(configurationFilePath)) {
            Logger.warning("No configuration file found. Creating...")

            createConfigurationFile()
        } else {
            Logger.info("Configuration file found.")
        }
    }

    private suspend fun createConfigurationFile() {
        withContext(Dispatchers.IO) {
            val defaultConfig = plugin.loader.getResourceAsStream(DEFAULT_CONFIG_FILE_RESOURCE_NAME)
                .defaultConfigLoaded()
                .readAllBytes()

            Files.createDirectories(configPath.parent)
            Files.createFile(configPath)
            Files.write(configPath, defaultConfig)
        }
    }

    private fun loadConfig(path: String): Config? {
        Logger.info("Loading configuration...")

        return try {
            val config: Config = ConfigLoaderBuilder
                .empty()
                .withClassLoader(plugin.loader)
                .addDefaultDecoders()
                .addDefaultPreprocessors()
                .addDefaultParamMappers()
                .addDefaultPropertySources()
                .addDefaultParsers()
                .addDecoder(CommonDecoder())
                .build()
                .loadConfigOrThrow(path)
            Logger.info("Configuration loaded successfully.")
            config
        } catch (error: ConfigException) {
            Logger.info("Configuration failed to load: ${error.message}")
            null
        }
    }

    fun saveConfig(config: Config) {
        val configYaml = Yaml.default.encodeToString(config)

        Files.writeString(configPath, configYaml)
    }
}
