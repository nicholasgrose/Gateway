package xyz.rose.gateway.minecraft.platform.fabric

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.slf4j.toKLogger
import net.fabricmc.api.ModInitializer
import org.koin.core.context.startKoin
import org.slf4j.LoggerFactory
import xyz.rose.gateway.core.EmbeddedGatewayEnvironment
import xyz.rose.gateway.core.config.YamlFileConfigSource
import xyz.rose.gateway.core.gatewayModule
import xyz.rose.gateway.minecraft.platform.fabric.server.FabricChatEventListeners
import xyz.rose.gateway.platform.discord.DiscordPlatform
import xyz.rose.gateway.platform.minecraft.common.MinecraftPlatform
import java.nio.file.Path
import kotlin.io.path.Path

/**
 * The mod entry point for the Gateway Fabric mod
 *
 * @constructor Create a new Fabric mod
 */
class GatewayMod : ModInitializer {
    override fun onInitialize() {
        val logger = LoggerFactory.getLogger("gateway").toKLogger()
        val configPath = Path("config/gateway/config.yml")

        startKoin {
            modules(newGatewayModule(logger, configPath))
        }

        FabricChatEventListeners.addChatListeners()
    }

    fun newGatewayModule(logger: KLogger, configPath: Path) = gatewayModule(
        EmbeddedGatewayEnvironment(
            logger = logger,
            platforms = listOf(
                MinecraftPlatform.definition(),
                DiscordPlatform.definition(),
            ),
            source = YamlFileConfigSource(configPath)
        )
    )
}
