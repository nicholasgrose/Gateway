package xyz.rose.gateway.core.config

import io.mockk.mockk
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test

/**
 * Tests for the [CombinedGatewayConfig]
 *
 * @constructor Create a new CombinedGatewayConfigTest
 */
class CombinedGatewayConfigTest {
    @Test
    fun `throws error when loading unregistered config`() {
        val config = CombinedGatewayConfig(mockk(), emptyList(), mockk())

        assertThrows<IllegalArgumentException> { config.getConfig<String>("unregistered") }
    }

    @Test
    fun `gets config for schema 1 on successful load`() {
        TODO("Implement this")
    }

    @Test
    fun `gets config for schema 2 on successful load`() {
        TODO("Implement this")
    }

    @Test
    fun `gets default config for schema 1 on unsuccessful load`() {
        TODO("Implement this")
    }

    @Test
    fun `gets default config for schema 2 on unsuccessful load`() {
        TODO("Implement this")
    }

    @Test
    fun `saves default config when source is uninitialized`() {
        TODO("Implement this")
    }
}
