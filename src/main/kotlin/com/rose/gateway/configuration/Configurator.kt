package com.rose.gateway.configuration

import com.kotlindiscord.kord.extensions.extensions.Extension
import com.rose.gateway.GatewayPlugin
import com.rose.gateway.Logger
import com.rose.gateway.bot.extensions.about.AboutExtension
import com.rose.gateway.bot.extensions.chat.ChatExtension
import com.rose.gateway.bot.extensions.list.ListExtension
import com.rose.gateway.bot.extensions.whitelist.WhitelistExtension
import com.rose.gateway.minecraft.server.Scheduler
import com.uchuhimo.konf.Config
import com.uchuhimo.konf.UnsetValueException
import com.uchuhimo.konf.source.yaml
import org.bukkit.Bukkit
import kotlin.reflect.KFunction

object Configurator {
    val config by lazy {
        runCatching {
            Config { addSpec(PluginSpec) }
                .from.yaml.watchFile("plugins/DiscordBot/DiscordBot.yaml")
        }.onFailure { error ->
            giveConfigErrorReport(
                error as Exception,
                "An error occurred while loading configuration! Please fix before starting again!"
            )
        }.getOrNull() ?: Config()
    }

    fun ensureProperConfiguration(): Boolean {
        return try {
            config.validateRequired()
            true
        } catch (error: UnsetValueException) {
            giveConfigErrorReport(error, "Configurations not set correctly! Please fix before starting again!")
            false
        }
    }

    private fun giveConfigErrorReport(error: Exception, additionalMessage: String) {
        Logger.log(additionalMessage)
        Logger.log(error.message ?: "No error message not found.")
        Scheduler.runTask {
            Bukkit.getServer().pluginManager.disablePlugin(GatewayPlugin.plugin)
        }
    }

    private val extensionSpecs = mapOf(
        ::WhitelistExtension to PluginSpec.BotSpec.EnabledExtensionsSpec.whitelistExtension,
        ::ListExtension to PluginSpec.BotSpec.EnabledExtensionsSpec.listExtension,
        ::AboutExtension to PluginSpec.BotSpec.EnabledExtensionsSpec.aboutExtension,
        ::ChatExtension to PluginSpec.BotSpec.EnabledExtensionsSpec.chatExtension
    )

    fun extensionEnabled(extension: KFunction<Extension>): Boolean {
        val spec = extensionSpecs[extension] ?: return false
        return config[spec]
    }
}
