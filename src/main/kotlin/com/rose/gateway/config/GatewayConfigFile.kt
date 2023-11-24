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

/**
 * Class that provides methods for interfacing with the Gateway config file
 *
 * @constructor Creates a gateway config file
 */
class GatewayConfigFile : KoinComponent {
    companion object {
        private const val DEFAULT_CONFIG_FILE_RESOURCE_NAME = "default_gateway_config.yaml"
        private const val DEFAULT_CONFIG_FILE_RESOURCE_PATH = "/$DEFAULT_CONFIG_FILE_RESOURCE_NAME"
        private const val CONFIG_FILE_NAME = "config.yaml"
    }

    private val plugin: GatewayPlugin by inject()

    private val pluginDirPath = plugin.dataFolder.path.replace("\\", "/")
    private val configPath = Path.of("$pluginDirPath/$CONFIG_FILE_NAME")

    /**
     * The default configuration that is used as an internal fallback
     */
    val defaultConfig =
        run {
            Logger.info("Loading fallback config...")

            loadConfig(DEFAULT_CONFIG_FILE_RESOURCE_PATH).notNullWithMissingDefaultConfigMessage()
        }

    /**
     * Loads the gateway config file, creating it if it is missing
     *
     * @return The loaded Gateway config object
     */
    suspend fun safelyLoadConfig(): Config {
        ensureConfigurationFileExists()

        val config = loadConfig(configPath.toString())

        return if (config == null) {
            Logger.warning("Fell back to default config.")

            defaultConfig
        } else {
            config
        }
    }

    /**
     * Asserts that a value is not null, raising an error with a message saying the default config file is missing
     *
     * @param T The type to guarantee the value is
     * @return The non-nullable type
     */
    private fun <T> T?.notNullWithMissingDefaultConfigMessage(): T {
        return this.notNull("default config resource does not exist to be loaded")
    }

    /**
     * Ensures that a configuration file exists to be loaded
     */
    private suspend fun ensureConfigurationFileExists() {
        Logger.info("Checking configuration file existence...")

        if (!Files.exists(configPath)) {
            Logger.warning("No configuration file found. Creating...")

            createConfigurationFile()
        } else {
            Logger.info("Configuration file found.")
        }
    }

    /**
     * Creates a new configuration file at the config path
     * The new file is a copy of the bundled default config file
     */
    private suspend fun createConfigurationFile() {
        withContext(Dispatchers.IO) {
            val defaultConfig =
                plugin.loader.getResourceAsStream(DEFAULT_CONFIG_FILE_RESOURCE_NAME)
                    .notNullWithMissingDefaultConfigMessage()
                    .readAllBytes()

            Files.createDirectories(configPath.parent)
            Files.createFile(configPath)
            Files.write(configPath, defaultConfig)
        }
    }

    /**
     * Loads the config from the provided path
     * This path may reference a bundled jar resource file
     *
     * @param path The path to load the config from
     * @return The loaded config or null, if it can't be loaded
     */
    private fun loadConfig(path: String): Config? {
        Logger.info("Loading configuration...")

        return try {
            val config: Config =
                ConfigLoaderBuilder
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
            Logger.warning("Configuration failed to load: ${error.message}")
            null
        }
    }

    /**
     * Saves the provided config object to the Gateway config file
     *
     * @param config The config to save
     */
    suspend fun saveConfig(config: Config) {
        val configYaml = Yaml.default.encodeToString(config)

        withContext(Dispatchers.IO) {
            Files.writeString(configPath, configYaml)
        }
    }
}
