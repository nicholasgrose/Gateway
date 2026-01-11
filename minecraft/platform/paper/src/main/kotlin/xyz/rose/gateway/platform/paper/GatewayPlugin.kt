package xyz.rose.gateway.platform.paper

import io.github.oshai.kotlinlogging.slf4j.toKLogger
import org.bukkit.plugin.java.JavaPlugin
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.mp.KoinPlatform.getKoin
import xyz.rose.gateway.core.EmbeddedGatewayEnvironment
import xyz.rose.gateway.core.GatewayApp
import xyz.rose.gateway.core.config.YamlFileConfigSource
import xyz.rose.gateway.core.gatewayModule
import xyz.rose.gateway.platform.discord.DiscordPlatform
import xyz.rose.gateway.platform.minecraft.common.MinecraftPlatform
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

        // This will eventually need to be saved off so we can "restart" Gateway by destroying and rebuilding this module
        val gatewayModule = newGatewayModule(configPath)

        startKoin {
            modules(gatewayModule)
        }

        getKoin().get<GatewayApp>().start()

        server.pluginManager.registerEvents(PaperEventListeners, this)
        pluginLogger.info { "Gateway started!" }
    }

    /**
     * Create the Gateway runtime module
     *
     * @param configPath The path to the config file
     */
    fun newGatewayModule(configPath: Path) = gatewayModule(
        EmbeddedGatewayEnvironment(
            logger = pluginLogger,
            platforms = listOf(MinecraftPlatform.definition(), DiscordPlatform.definition()),
            source = YamlFileConfigSource(configPath)
        )
    )

    override fun onDisable() {
        getKoin().get<GatewayApp>().stop()
        stopKoin()

        pluginLogger.info { "Gateway stopped!" }
    }
}
