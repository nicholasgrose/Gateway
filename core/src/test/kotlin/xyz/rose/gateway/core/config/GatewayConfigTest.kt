package xyz.rose.gateway.core.config

import kotlinx.serialization.Serializable
import kotlin.test.Test
import kotlin.test.assertEquals

class GatewayConfigTest {


    @Test
    fun canLoadTopLevel() {
        assertEquals(true, true)
    }

    @Serializable
    data class TopLevel(val jacob: String)
}
