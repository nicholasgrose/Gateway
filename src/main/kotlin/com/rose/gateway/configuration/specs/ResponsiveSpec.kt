package com.rose.gateway.configuration.specs

import com.rose.gateway.GatewayPlugin

interface ResponsiveSpec {
    fun setConfigChangeActions(plugin: GatewayPlugin)
}
