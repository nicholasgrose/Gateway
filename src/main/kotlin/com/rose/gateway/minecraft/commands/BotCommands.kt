package com.rose.gateway.minecraft.commands

import com.rose.gateway.GatewayPlugin
import com.rose.gateway.Logger

object BotCommands {
    fun restartBot(): Boolean {
        Logger.log("Restarting the Discord bot!")
        GatewayPlugin.plugin.restartBot()
        return true
    }
}
