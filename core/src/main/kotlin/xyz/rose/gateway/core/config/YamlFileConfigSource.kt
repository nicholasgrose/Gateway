package xyz.rose.gateway.core.config

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import net.mamoe.yamlkt.Yaml
import java.io.File
import java.io.IOException
import java.nio.file.Path

/**
 * A config source that is a single YAML file
 *
 * @constructor Creates a new YAML file source
 *
 * @param path The path to the YAML file
 */
class YamlFileConfigSource<T>(path: Path) : GatewayConfigSource<T> {
    val file: File = path.toFile()

    override fun load(serializer: KSerializer<T>): GatewayConfigLoadResult<T> {
        if (!file.exists()) {
            return GatewayConfigLoadResult.Uninitialized()
        }

        return try {
            val configText = file.readText()
            val data = Yaml.decodeFromString(serializer, configText)

            GatewayConfigLoadResult.Success(data)
        } catch (e: IOException) {
            GatewayConfigLoadResult.Failure(e)
        } catch (e: SerializationException) {
            GatewayConfigLoadResult.Failure(e)
        }
    }

    override fun save(serializer: KSerializer<T>, data: T): GatewayConfigSaveResult {
        return try {
            file.writeText(Yaml.encodeToString(serializer, data))

            GatewayConfigSaveResult.Success()
        } catch (e: IOException) {
            GatewayConfigSaveResult.Failure(e)
        } catch (e: SerializationException) {
            GatewayConfigSaveResult.Failure(e)
        }
    }
}
