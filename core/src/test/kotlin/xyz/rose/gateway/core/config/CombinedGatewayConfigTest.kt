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
    fun testConfig(
        loadResult: GatewayConfigLoadResult<Map<String, Any>> = GatewayConfigLoadResult.Success(emptyMap()),
        serializer: KSerializer<Map<String, Any>> = mockk(),
        source: GatewayConfigSource<Map<String, Any>> = mockk {
            every { load(serializer) } returns loadResult
            every { save(serializer, any()) } returns GatewayConfigSaveResult.Success()
        },
        schemas: List<GatewayConfigSchema<Any>> = emptyList(),
    ): CombinedGatewayConfig {
        val config = CombinedGatewayConfig(
            source = source,
            platforms = schemas.map { GatewayPlatformDefinition(it, mockk()) },
            logger = mockk(relaxed = true),
            serializer = serializer
        )

        return config
    }

    @Test
    fun `throws error when loading unregistered config`() {
        val config = testConfig(GatewayConfigLoadResult.Success(emptyMap()))

        assertThrows<NoSuchElementException> { config.getConfig<String>("unregistered") }
    }

    @Test
    fun `gets config for schema 1 on successful load`() {
        @Suppress("UNCHECKED_CAST")
        val config = testConfig(
            loadResult = GatewayConfigLoadResult.Success(mapOf("schema1" to "hello")),
            schemas = listOf(
                mockk {
                    every { key } returns "schema1"
                    every { injectableType } returns String::class as KClass<Any>
                }
            )
        )

        assertEquals("hello", config.getConfig("schema1"))
    }

    @Test
    fun `gets config for schema 2 on successful load`() {
        @Suppress("UNCHECKED_CAST")
        val config = testConfig(
            loadResult = GatewayConfigLoadResult.Success(mapOf("schema1" to "hello", "schema2" to "world")),
            schemas = listOf(
                mockk {
                    every { key } returns "schema1"
                    every { injectableType } returns String::class as KClass<Any>
                },
                mockk {
                    every { key } returns "schema2"
                    every { injectableType } returns String::class as KClass<Any>
                }
            )
        )

        assertEquals("world", config.getConfig("schema2"))
    }

    @Test
    fun `gets default config for schema 1 on unsuccessful load`() {
        @Suppress("UNCHECKED_CAST")
        val config = testConfig(
            loadResult = GatewayConfigLoadResult.Failure(Exception("Failed to load config")),
            schemas = listOf(
                mockk {
                    every { key } returns "schema1"
                    every { default } returns "hello"
                    every { injectableType } returns String::class as KClass<Any>
                }
            )
        )

        assertEquals("hello", config.getConfig("schema1"))
    }

    @Test
    fun `gets default config for schema 2 on unsuccessful load`() {
        @Suppress("UNCHECKED_CAST")
        val config = testConfig(
            loadResult = GatewayConfigLoadResult.Failure(Exception("Failed to load config")),
            schemas = listOf(
                mockk {
                    every { key } returns "schema1"
                    every { default } returns "hello"
                    every { injectableType } returns String::class as KClass<Any>
                },
                mockk {
                    every { key } returns "schema2"
                    every { default } returns "world"
                    every { injectableType } returns String::class as KClass<Any>
                }
            )
        )

        assertEquals("world", config.getConfig("schema2"))
    }

    @Test
    fun `gets default config for schema 1 when source is uninitialized`() {
        @Suppress("UNCHECKED_CAST")
        val config = testConfig(
            loadResult = GatewayConfigLoadResult.Uninitialized(),
            schemas = listOf(
                mockk {
                    every { key } returns "schema1"
                    every { default } returns "hello"
                    every { injectableType } returns String::class as KClass<Any>
                }
            )
        )

        assertEquals("hello", config.getConfig("schema1"))
    }


    @Test
    fun `gets default config for schema 2 when source is uninitialized`() {
        @Suppress("UNCHECKED_CAST")
        val config = testConfig(
            loadResult = GatewayConfigLoadResult.Uninitialized(),
            schemas = listOf(
                mockk {
                    every { key } returns "schema1"
                    every { default } returns "hello"
                    every { injectableType } returns String::class as KClass<Any>
                },
                mockk {
                    every { key } returns "schema2"
                    every { default } returns "world"
                    every { injectableType } returns String::class as KClass<Any>
                }
            )
        )

        assertEquals("world", config.getConfig("schema2"))
    }

    @Test
    fun `saves default config to source on uninitialized load`() {
        val serializer = mockk<KSerializer<Map<String, Any>>>()
        val source = mockk<GatewayConfigSource<Map<String, Any>>> {
            every { load(serializer) } returns GatewayConfigLoadResult.Uninitialized()
            every { save(serializer, any()) } returns GatewayConfigSaveResult.Success()
        }
        @Suppress("UNCHECKED_CAST")
        testConfig(
            source = source,
            serializer = serializer,
            schemas = listOf(
                mockk {
                    every { key } returns "schema1"
                    every { default } returns "hello"
                    every { injectableType } returns String::class as KClass<Any>
                }
            )
        )

        verify(exactly = 1) { source.save(serializer, mapOf("schema1" to "hello")) }
    }
}
