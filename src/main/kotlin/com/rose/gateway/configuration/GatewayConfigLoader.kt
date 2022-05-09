package com.rose.gateway.configuration

import com.rose.gateway.GatewayPlugin
import com.rose.gateway.Logger
import com.rose.gateway.configuration.markers.CommonDecoder
import com.rose.gateway.configuration.schema.Config
import com.sksamuel.hoplite.ConfigException
import com.sksamuel.hoplite.ConfigLoaderBuilder
import com.sksamuel.hoplite.addResourceSource
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.nio.file.Files
import java.nio.file.Path

class GatewayConfigLoader(
    private val plugin: GatewayPlugin,
    configFile: String,
    private val configUrl: String
) {
    private val configPath = Path.of(configFile)

    suspend fun loadOrCreateConfig(): Config? {
        if (ensureConfigurationFileExists(configPath)) {
            val configuration = loadConfig(configPath)

            if (configuration != null) {
                return configuration
            }
        }

        return null
    }

    private suspend fun ensureConfigurationFileExists(configurationFilePath: Path): Boolean {
        Logger.logInfo("Checking configuration file existence...")

        if (!Files.exists(configurationFilePath)) {
            Logger.logInfo("No configuration file found. Downloading...")

            val configurationFileCreated = createConfigurationFile()

            if (!configurationFileCreated) {
                Logger.logInfo("Failed to create new configuration file.")

                return false
            } else {
                Logger.logInfo("Successfully created new configuration file.")
            }
        } else {
            Logger.logInfo("Configuration file found.")
        }

        return true
    }

    private suspend fun createConfigurationFile(): Boolean {
        val result = plugin.httpClient.get(configUrl)

        return if (result.status == HttpStatusCode.OK) {
            withContext(Dispatchers.IO) {
                Files.createDirectories(configPath.parent)
                Files.createFile(configPath)
                Files.writeString(configPath, result.bodyAsText())
            }
            true
        } else {
            Logger.logInfo("Could not get configuration file! You will need download or create it manually.")
            false
        }
    }

    private fun loadConfig(path: Path): Config? {
        Logger.logInfo("Loading configuration...")
        return try {
            val config = ConfigLoaderBuilder.default()
                .addDecoder(CommonDecoder())
                .addResourceSource(path.toString())
                .build()
                .loadConfigOrThrow<Config>()
            Logger.logInfo("Configuration loaded successfully.")
            config
        } catch (error: ConfigException) {
            Logger.logInfo("Configuration failed to load: ${error.message}")
            null
        }
    }
}
