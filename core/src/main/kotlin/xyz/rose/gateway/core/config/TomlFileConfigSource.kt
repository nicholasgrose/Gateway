package xyz.rose.gateway.core.config

import com.akuleshov7.ktoml.Toml
import com.akuleshov7.ktoml.TomlInputConfig
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import java.io.File
import java.io.IOException
import java.nio.file.Path

/**
 * A config source that is a single TOML file
 *
 * @constructor Creates a new TOML file source
 *
 * @param path The path to the TOML file
 */
class TomlFileConfigSource<T>(
    path: Path,
    private val toml: Toml = Toml(inputConfig = TomlInputConfig(ignoreUnknownNames = true))
) : ConfigSource<T> {
    /**
     * The file to read and write config data from
     */
    val file: File = path.toFile()

    override fun load(serializer: KSerializer<T>): GatewayConfigLoadResult<T> {
        if (!file.exists()) {
            return GatewayConfigLoadResult.Uninitialized()
        }

        return try {
            val configText = file.readText()
            val data = toml.decodeFromString(serializer, configText)

            GatewayConfigLoadResult.Success(data)
        } catch (e: IOException) {
            GatewayConfigLoadResult.Failure(e)
        } catch (e: SerializationException) {
            GatewayConfigLoadResult.Failure(e)
        }
    }

    override fun save(serializer: KSerializer<T>, data: T): GatewayConfigSaveResult {
        return try {
            file.parentFile?.mkdirs()
            file.writeText(toml.encodeToString(serializer, data))

            GatewayConfigSaveResult.Success()
        } catch (e: IOException) {
            GatewayConfigSaveResult.Failure(e)
        } catch (e: SerializationException) {
            GatewayConfigSaveResult.Failure(e)
        }
    }
}
