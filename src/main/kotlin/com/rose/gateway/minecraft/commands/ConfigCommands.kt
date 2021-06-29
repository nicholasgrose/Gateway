package com.rose.gateway.minecraft.commands

import com.rose.gateway.minecraft.commands.framework.CommandContext

object ConfigCommands {
    fun setConfiguration(context: CommandContext): Boolean {
        context.sender.sendMessage("config set")
        return true
    }

    fun addConfiguration(context: CommandContext): Boolean {
        context.sender.sendMessage("config add")
        return true
    }

    fun removeConfiguration(context: CommandContext): Boolean {
        context.sender.sendMessage("config remove")
        return true
    }

    fun configurationHelp(context: CommandContext): Boolean {
        context.sender.sendMessage("config help")
        return true
    }
}
