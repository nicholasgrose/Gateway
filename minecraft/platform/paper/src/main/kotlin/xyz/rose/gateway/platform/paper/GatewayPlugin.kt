package xyz.rose.gateway.platform.paper

import io.github.oshai.kotlinlogging.slf4j.toKLogger
import org.bukkit.plugin.java.JavaPlugin
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.mp.KoinPlatform.getKoin
import xyz.rose.gateway.core.EmbeddedGatewayApp
import xyz.rose.gateway.core.GatewayApp
import xyz.rose.gateway.core.GatewayEnvironment
import xyz.rose.gateway.core.config.CombinedGatewayConfigBuilder
import xyz.rose.gateway.core.config.YamlFileConfigSource
import xyz.rose.gateway.core.gateway
import xyz.rose.gateway.platform.discord.DiscordPlatformProvider
import xyz.rose.gateway.platform.minecraft.common.MinecraftPlatformProvider
import java.nio.file.Path

/**
 * The Gateway Paper plugin
 *
 * @constructor Create a new Gateway Paper plugin
 */
@Suppress("unused")
class GatewayPlugin : JavaPlugin() {
    /**
     * Create a KLogger to wrap the SLF4J logger and make it more Kotlin-friendly
     */
    val pluginLogger = slF4JLogger.toKLogger()

    override fun onEnable() {
        val configPath = dataFolder.toPath().resolve("config.yml")

        initializeGateway(configPath)
        getKoin().get<GatewayApp>().start()

        pluginLogger.info { "Gateway started!" }
    }

    /**
     * Initializes the Gateway application with the specified configuration file path.
     *
     * This method sets up the Gateway runtime environment by loading the configuration from the provided YAML file,
     * creating the relevant Gateway components, and registering platform providers.
     *
     * @param configPath The path to the YAML configuration file that specifies the Gateway application settings.
     */
    private fun initializeGateway(configPath: Path) {
        startKoin {
            gateway(
                GatewayEnvironment(
                    logger = pluginLogger,
                    appProvider = { EmbeddedGatewayApp() },
                    configBuilder = CombinedGatewayConfigBuilder(
                        YamlFileConfigSource(configPath)
                    ),
                    providers = listOf(MinecraftPlatformProvider(), DiscordPlatformProvider())
                )
            )
        }
    }

    override fun onDisable() {
        getKoin().get<GatewayApp>().stop()
        stopKoin()

        pluginLogger.info { "Gateway stopped!" }
    }
}
