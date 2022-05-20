package com.rose.gateway.configuration

import com.rose.gateway.configuration.schema.AboutConfig
import com.rose.gateway.configuration.schema.BotConfig
import com.rose.gateway.configuration.schema.ChatConfig
import com.rose.gateway.configuration.schema.Config
import com.rose.gateway.configuration.schema.ExtensionsConfig
import com.rose.gateway.configuration.schema.IpConfig
import com.rose.gateway.configuration.schema.ListConfig
import com.rose.gateway.configuration.schema.MinecraftConfig
import com.rose.gateway.configuration.schema.WhitelistConfig

val DEFAULT_CONFIG = Config(
    bot = BotConfig(
        token = "NONE",
        botChannels = listOf(),
        extensions = ExtensionsConfig(
            about = AboutConfig(
                enabled = true
            ),
            chat = ChatConfig(
                enabled = true
            ),
            list = ListConfig(
                enabled = true
            ),
            ip = IpConfig(
                enabled = true,
                displayIp = "NONE"
            ),
            whitelist = WhitelistConfig(
                enabled = true
            )
        )
    ),
    minecraft = MinecraftConfig(
        primaryColor = "#56EE5C",
        secondaryColor = "#7289DA",
        tertiaryColor = "#F526ED",
        warningColor = "#EB4325"
    )
)
