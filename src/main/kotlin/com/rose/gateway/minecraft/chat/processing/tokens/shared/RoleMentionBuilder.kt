package com.rose.gateway.minecraft.chat.processing.tokens.shared

import com.rose.gateway.GatewayPlugin
import com.rose.gateway.minecraft.chat.processing.tokens.TokenProcessingResult
import kotlinx.coroutines.flow.toSet

class RoleMentionBuilder(private val plugin: GatewayPlugin) {
    private val resultBuilder = ResultBuilder(plugin)

    suspend fun createRoleMention(nameString: String): TokenProcessingResult {
        for (guild in plugin.discordBot.botGuilds) {
            for (role in guild.roles.toSet()) {
                val minecraftText = "@${role.name}"
                val discordText = "<@&${role.id}>"

                if (role.name == nameString) return resultBuilder.mentionResult(minecraftText, discordText)
            }
        }

        return resultBuilder.errorResult("@$nameString")
    }
}
