package xyz.rose.gateway.core.config

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.serialization.KSerializer
import org.junit.jupiter.api.assertThrows
import xyz.rose.gateway.core.platform.GatewayPlatformDefinition
import kotlin.reflect.KClass
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Tests for the [CombinedGatewayConfig]
 *
 * @constructor Create a new CombinedGatewayConfigTest
 */
class CombinedGatewayConfigTest {
    private fun createSchema(
        key: String,
        default: Any = "default",
        type: KClass<*> = String::class
    ): ConfigSchema<Any> = mockk {
        every { this@mockk.key } returns key
        every { this@mockk.default } returns default
        @Suppress("UNCHECKED_CAST")
        every { this@mockk.injectableType } returns type as KClass<Any>
    }

    private fun testConfig(
        loadResult: GatewayConfigLoadResult<Map<String, Any>> = GatewayConfigLoadResult.Success(emptyMap()),
        serializer: KSerializer<Map<String, Any>> = mockk(),
        source: ConfigSource<Map<String, Any>> = mockk {
            every { load(serializer) } returns loadResult
            every { save(serializer, any()) } returns GatewayConfigSaveResult.Success()
        },
        schemas: List<ConfigSchema<Any>> = emptyList(),
    ): CombinedGatewayConfig = CombinedGatewayConfig(
        source = source,
        platforms = schemas.map { GatewayPlatformDefinition("test", it.key, it, mockk()) },
        logger = mockk(relaxed = true),
        serializer = serializer
    )

    @Test
    fun `throws error when loading unregistered config`() {
        val config = testConfig()

        assertThrows<NoSuchElementException> { config.getConfig<String>("unregistered") }
    }

    @Test
    fun `gets config for schema 1 on successful load`() {
        val config = testConfig(
            loadResult = GatewayConfigLoadResult.Success(mapOf("schema1" to "hello")),
            schemas = listOf(createSchema("schema1"))
        )

        assertEquals("hello", config.getConfig("schema1"))
    }

    @Test
    fun `gets config for schema 2 on successful load`() {
        val config = testConfig(
            loadResult = GatewayConfigLoadResult.Success(mapOf("schema1" to "hello", "schema2" to "world")),
            schemas = listOf(createSchema("schema1"), createSchema("schema2"))
        )

        assertEquals("world", config.getConfig("schema2"))
    }

    @Test
    fun `gets default config for schema 1 on unsuccessful load`() {
        val config = testConfig(
            loadResult = GatewayConfigLoadResult.Failure(Exception("Failed to load config")),
            schemas = listOf(createSchema("schema1", default = "hello"))
        )

        assertEquals("hello", config.getConfig("schema1"))
    }

    @Test
    fun `gets default config for schema 2 on unsuccessful load`() {
        val config = testConfig(
            loadResult = GatewayConfigLoadResult.Failure(Exception("Failed to load config")),
            schemas = listOf(
                createSchema("schema1", default = "hello"),
                createSchema("schema2", default = "world")
            )
        )

        assertEquals("world", config.getConfig("schema2"))
    }

    @Test
    fun `gets default config for schema 1 when source is uninitialized`() {
        val config = testConfig(
            loadResult = GatewayConfigLoadResult.Uninitialized(),
            schemas = listOf(createSchema("schema1", default = "hello"))
        )

        assertEquals("hello", config.getConfig("schema1"))
    }

    @Test
    fun `gets default config for schema 2 when source is uninitialized`() {
        val config = testConfig(
            loadResult = GatewayConfigLoadResult.Uninitialized(),
            schemas = listOf(
                createSchema("schema1", default = "hello"),
                createSchema("schema2", default = "world")
            )
        )

        assertEquals("world", config.getConfig("schema2"))
    }

    @Test
    fun `saves default config to source on uninitialized load`() {
        val serializer = mockk<KSerializer<Map<String, Any>>>()
        val source = mockk<ConfigSource<Map<String, Any>>> {
            every { load(serializer) } returns GatewayConfigLoadResult.Uninitialized()
            every { save(serializer, any()) } returns GatewayConfigSaveResult.Success()
        }

        testConfig(
            source = source,
            serializer = serializer,
            schemas = listOf(createSchema("schema1", default = "hello"))
        )

        verify(exactly = 1) { source.save(serializer, mapOf("schema1" to "hello")) }
    }
}
