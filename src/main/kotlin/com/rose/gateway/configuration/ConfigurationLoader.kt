package com.rose.gateway.configuration

import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import com.rose.gateway.Logger
import com.uchuhimo.konf.Config
import com.uchuhimo.konf.Spec
import com.uchuhimo.konf.source.yaml
import java.nio.file.Files
import java.nio.file.Path

class ConfigurationLoader(
    private val baseSpec: Spec,
    private val configurationFile: String,
    private val exampleConfigurationUrl: String
) {
    private val configurationFilePath = Path.of(configurationFile)

    fun loadOrCreateConfig(): Config? {
        Logger.log("Checking configuration file existence.")
        if (!Files.exists(configurationFilePath)) {
            Logger.log("No configuration file found. Downloading...")
            val configurationFileCreated = createConfigurationFile()
            if (!configurationFileCreated) {
                Logger.log("Failed to create new configuration file.")
                return null
            } else {
                Logger.log("Successfully created new configuration file.")
            }
        } else {
            Logger.log("Configuration file found.")
        }

        val configuration = loadConfig() ?: return null

        return if (configurationIsValid(configuration)) configuration
        else null
    }

    private fun createConfigurationFile(): Boolean {
        val (_, _, result) = exampleConfigurationUrl
            .httpGet()
            .responseString()

        return when (result) {
            is Result.Success -> {
                Files.writeString(configurationFilePath, result.value)
                Logger.log("Successfully created configuration file!")
                true
            }
            is Result.Failure -> {
                Logger.log("Could not get configuration file! You will need download or create it manually.")
                false
            }
        }
    }

    private fun loadConfig(): Config? {
        Logger.log("Loading configuration...")
        return try {
            val loadedConfig = Config { addSpec(baseSpec) }
                .from.yaml.file(configurationFile)
            Logger.log("Configuration loaded successfully.")
            loadedConfig
        } catch (error: Throwable) {
            Logger.log("Configuration failed to load: ${error.message}")
            null
        }
    }

    private fun configurationIsValid(configuration: Config): Boolean {
        val isValid = configuration.containsRequired()

        if (isValid) {
            Logger.log("Configuration is valid.")
        } else {
            Logger.log("Configuration invalid.")
        }

        return isValid
    }
}
