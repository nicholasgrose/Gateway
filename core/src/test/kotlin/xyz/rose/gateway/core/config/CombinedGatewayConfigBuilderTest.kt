package xyz.rose.gateway.core.config

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.test.Test
import kotlin.test.assertIs

/**
 * Tests the [CombinedGatewayConfigBuilder].
 *
 * @constructor Create a new Combined gateway config builder test
 */
class CombinedGatewayConfigBuilderTest {
    /**
     * Gets a builder to test.
     *
     * @return The builder
     */
    fun getBuilder() = CombinedGatewayConfigBuilder(mockk())

    /**
     * Get a mock schema that returns a particular key
     *
     * @param desiredKey The key to return
     */
    fun schemaWithKey(desiredKey: String) = mockk<GatewayConfigSchema<Any>> {
        every { key } returns desiredKey
    }

    @Test
    fun `builder returns expected config type`() {
        val builder = getBuilder()

        val config = builder.build()

        assertIs<CombinedGatewayConfig>(config, "built config should be of type CombinedGatewayConfig")
    }

    @Test
    fun `maps correctly without schemas`() {
        val builder = getBuilder()

        val config = builder.build() as CombinedGatewayConfig

        assert(config.schemas.isEmpty()) {
            "no schemas should exist"
        }
    }

    @Test
    fun `maps one schema correctly`() {
        val builder = getBuilder()
        val key = "1"
        builder.schemas.add(schemaWithKey(key))

        val config = builder.build() as CombinedGatewayConfig

        assertEquals(1, config.schemas.size) {
            "only one schema should exist"
        }

        assertEquals(key, config.schemas[key]?.key) {
            "the key of the schema map should match the schema's key"
        }
    }

    @Test
    fun `maps many schemas correctly`() {
        val builder = getBuilder()
        val key1 = "1"
        val key2 = "2"
        val key3 = "3"
        builder.schemas.addAll(
            arrayOf(
                schemaWithKey(key1),
                schemaWithKey(key2),
                schemaWithKey(key3),
            )
        )

        val config = builder.build() as CombinedGatewayConfig

        assertEquals(3, config.schemas.size) {
            "only three schemas should exist"
        }

        config.schemas.forEach { entry ->
            assertEquals(entry.key, entry.value.key) {
                "all keys of the schema map should match the schema's key"
            }
        }
    }
}
