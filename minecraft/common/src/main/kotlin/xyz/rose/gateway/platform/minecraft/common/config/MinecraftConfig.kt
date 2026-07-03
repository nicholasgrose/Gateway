package xyz.rose.gateway.platform.minecraft.common.config

import kotlinx.serialization.Serializable

@Serializable
data class MinecraftConfig(
    val address: ServerAddress,
    val chat: ChatColors
)

@Serializable
data class ServerAddress(val host: String, val port: Int)

@Serializable
data class ChatColors(
    val primaryColor: String,
    val secondaryColor: String,
    val tertiaryColor: String,
    val warningColor: String,
)
