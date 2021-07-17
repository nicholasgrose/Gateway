package com.rose.gateway.configuration

import com.uchuhimo.konf.ConfigSpec

open class CommonExtensionSpec : ConfigSpec() {
    val enabled by optional(true, description = "Whether the ${super.prefix} extension is enabled.")
}
