package com.rose.gateway.config

import com.charleskorn.kaml.Yaml
import com.rose.gateway.GatewayPlugin
import com.rose.gateway.config.markers.CommonDecoder
import com.rose.gateway.config.schema.Config
import com.rose.gateway.minecraft.logging.Logger
import com.sksamuel.hoplite.ConfigException
import com.sksamuel.hoplite.ConfigLoaderBuilder
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.nio.file.Files
import java.nio.file.Path

class GatewayConfigLoader : KoinComponent {
    private val plugin: GatewayPlugin by inject()
    private val httpClient: HttpClient by inject()

    companion object {
        const val CONFIG_FILE_NAME = "config.yaml"
        const val REPOSITORY_RAW_URL = "https://raw.githubusercontent.com/nicholasgrose/Gateway"
    }

    private val pluginDirPath = plugin.dataFolder.path.replace("\\", "/")
    private val configPath = Path.of("$pluginDirPath/$CONFIG_FILE_NAME")
    private val configUrl = "$REPOSITORY_RAW_URL/v${plugin.description.version}/examples/$CONFIG_FILE_NAME"

    suspend fun loadOrCreateConfig(): Config {
        if (ensureConfigurationFileExists(configPath)) {
            val configuration = loadConfig(configPath)

            if (configuration != null) {
                return configuration
            }
        }

        return DEFAULT_CONFIG
    }

    private suspend fun ensureConfigurationFileExists(configurationFilePath: Path): Boolean {
        Logger.info("Checking configuration file existence...")

        if (!Files.exists(configurationFilePath)) {
            Logger.info("No configuration file found. Downloading...")

            val configurationFileCreated = createConfigurationFile()

            if (!configurationFileCreated) {
                Logger.info("Failed to create new configuration file.")

                return false
            } else {
                Logger.info("Successfully created new configuration file.")
            }
        } else {
            Logger.info("Configuration file found.")
        }

        return true
    }

    private suspend fun createConfigurationFile(): Boolean {
        val result = httpClient.get(configUrl)

        return if (result.status == HttpStatusCode.OK) {
            withContext(Dispatchers.IO) {
                Files.createDirectories(configPath.parent)
                Files.createFile(configPath)
                Files.writeString(configPath, result.bodyAsText())
            }
            true
        } else {
            Logger.info("Could not get configuration file! You will need download or create it manually.")
            false
        }
    }

    private fun loadConfig(path: Path): Config? {
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
                .loadConfigOrThrow(path.toString())
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