package xyz.rose.gateway.platform.paper

import org.bukkit.plugin.java.JavaPlugin

@Suppress("unused")
class GatewayPlugin : JavaPlugin() {
    override fun onEnable() {
        logger.info("Gateway started!")
    }

    override fun onDisable() {
        logger.info("Gateway stopped!")
    }
}
