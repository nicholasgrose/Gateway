package com.rose.gateway.config

import com.rose.gateway.config.schema.AboutConfig
import com.rose.gateway.config.schema.BotConfig
import com.rose.gateway.config.schema.ChatConfig
import com.rose.gateway.config.schema.Config
import com.rose.gateway.config.schema.ExtensionsConfig
import com.rose.gateway.config.schema.IpConfig
import com.rose.gateway.config.schema.ListConfig
import com.rose.gateway.config.schema.MinecraftConfig
import com.rose.gateway.config.schema.WhitelistConfig

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
