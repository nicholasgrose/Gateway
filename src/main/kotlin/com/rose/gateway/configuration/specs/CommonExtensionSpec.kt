package com.rose.gateway.configuration.specs

import com.uchuhimo.konf.ConfigSpec

open class CommonExtensionSpec(private val extensionName: String) : ConfigSpec() {
    val enabled by optional(true, description = "Whether the ${super.prefix} extension is enabled.")
}
