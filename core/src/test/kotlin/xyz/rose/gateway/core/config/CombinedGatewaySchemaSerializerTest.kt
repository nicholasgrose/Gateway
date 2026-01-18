package xyz.rose.gateway.core.config

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import net.mamoe.yamlkt.Yaml
import org.junit.jupiter.api.assertThrows
import kotlin.reflect.KClass
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Tests for [CombinedGatewaySchemaSerializer]
 */
class CombinedGatewaySchemaSerializerTest {

    @Serializable
    data class TestConfig1(val name: String, val value: Int)

    @Serializable
    data class TestConfig2(val enabled: Boolean)

    class TestSchema<T : Any>(
        override val key: String,
        override val serializer: KSerializer<T>,
        override val injectableType: KClass<T>,
        override val default: T
    ) : GatewayConfigSchema<T>

    @Suppress("UNCHECKED_CAST")
    private val schema1: GatewayConfigSchema<Any> = TestSchema(
        "test1",
        TestConfig1.serializer(),
        TestConfig1::class,
        TestConfig1("default", 0)
    ) as GatewayConfigSchema<Any>

    @Suppress("UNCHECKED_CAST")
    private val schema2: GatewayConfigSchema<Any> =
        TestSchema("test2", TestConfig2.serializer(), TestConfig2::class, TestConfig2(true)) as GatewayConfigSchema<Any>

    private val schemas = mapOf(
        "test1" to schema1,
        "test2" to schema2
    )

    private val serializer = CombinedGatewaySchemaSerializer(schemas)

    @Test
    fun `test serialization`() {
        val data = mapOf(
            "test1" to TestConfig1("hello", 123),
            "test2" to TestConfig2(false)
        )

        val serialized = Yaml.encodeToString(serializer, data)
        // YAML format might differ, but we can verify by decoding it back
        val deserialized = Yaml.decodeFromString(serializer, serialized)
        assertEquals(data, deserialized)
    }

    @Test
    fun `test deserialization`() {
        val input = """
            test1:
              name: "world"
              value: 456
            test2:
              enabled: true
        """.trimIndent()
        val deserialized = Yaml.decodeFromString(serializer, input)

        assertEquals(2, deserialized.size)
        assertEquals(TestConfig1("world", 456), deserialized["test1"] as TestConfig1)
        assertEquals(TestConfig2(true), deserialized["test2"] as TestConfig2)
    }

    @Test
    fun `test serialization missing key throws`() {
        val data = mapOf(
            "test1" to TestConfig1("missing", 0)
        )

        assertThrows<SerializationException> {
            Yaml.encodeToString(serializer, data)
        }
    }

    @Test
    fun `test deserialization unknown key`() {
        val input = """
            test1:
              name: "world"
              value: 456
            test2:
              enabled: true
            unknown:
              foo: "bar"
        """.trimIndent()

        // YamlKt usually ignores unknown keys by default if they are not in the descriptor
        val deserialized = Yaml.decodeFromString(serializer, input)
        assertEquals(2, deserialized.size)
        assertEquals(TestConfig1("world", 456), deserialized["test1"] as TestConfig1)
        assertEquals(TestConfig2(true), deserialized["test2"] as TestConfig2)
    }
}
