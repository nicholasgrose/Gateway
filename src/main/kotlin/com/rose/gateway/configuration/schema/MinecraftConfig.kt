package com.rose.gateway.configuration.schema

import com.rose.gateway.configuration.markers.ConfigItem
import com.rose.gateway.configuration.markers.ConfigObject

data class MinecraftConfig(
    @ConfigItem("Used for labels and Discord mentions in-game.")
    var primaryColor: String,
    @ConfigItem("Used for differentiated text elements and names of Discord users in-game.")
    var secondaryColor: String,
    @ConfigItem("Used for labelling configuration paths.")
    var tertiaryColor: String,
    @ConfigItem("Used for marking configurations that can be null.")
    var warningColor: String
) : ConfigObject