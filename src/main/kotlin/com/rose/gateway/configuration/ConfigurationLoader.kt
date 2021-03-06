package com.rose.gateway.configuration

import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import com.rose.gateway.Logger
import com.uchuhimo.konf.Config
import com.uchuhimo.konf.Spec
import com.uchuhimo.konf.source.yaml
import org.yaml.snakeyaml.error.YAMLException
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path

class ConfigurationLoader(
    private val baseSpec: Spec,
    private val configurationFile: String,
    private val exampleConfigurationUrl: String
) {
    private val configurationFilePath = Path.of(configurationFile)

    fun loadOrCreateConfig(): Config? {
        if (ensureConfigurationFileExists(configurationFilePath)) {
            val configuration = loadConfig()

            if (configuration != null && configurationIsValid(configuration)) {
                return configuration
            }
        }

        return null
    }

    private fun ensureConfigurationFileExists(configurationFilePath: Path): Boolean {
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

    private fun createConfigurationFile(): Boolean {
        val (_, _, result) = exampleConfigurationUrl
            .httpGet()
            .responseString()

        return when (result) {
            is Result.Success -> {
                Files.createDirectories(configurationFilePath.parent)
                Files.createFile(configurationFilePath)
                Files.writeString(configurationFilePath, result.value)
                true
            }
            is Result.Failure -> {
                Logger.logInfo("Could not get configuration file! You will need download or create it manually.")
                false
            }
        }
    }

    private fun loadConfig(): Config? {
        Logger.logInfo("Loading configuration...")
        return try {
            val loadedConfig = Config { addSpec(baseSpec) }
                .from.yaml.file(configurationFile)
            Logger.logInfo("Configuration loaded successfully.")
            loadedConfig
        } catch (error: YAMLException) {
            Logger.logInfo("Configuration failed to load due to problem with YAML: ${error.message}")
            null
        } catch (error: IOException) {
            Logger.logInfo("Configuration failed to load due to problem with I/O: ${error.message}")
            null
        }
    }

    private fun configurationIsValid(configuration: Config): Boolean {
        val isValid = configuration.containsRequired()

        if (isValid) {
            Logger.logInfo("Configuration is valid.")
        } else {
            Logger.logInfo("Configuration invalid.")
        }

        return isValid
    }
}
