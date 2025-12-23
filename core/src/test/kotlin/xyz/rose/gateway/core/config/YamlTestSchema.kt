package xyz.rose.gateway.core.config

import kotlinx.serialization.Serializable

/**
 * Schemas for testing the YAML file configuration source
 *
 * @constructor Create a new YAML test schema
 */
sealed class YamlTestSchema {
    /**
     * An invalid schema
     */
    @Serializable
    data class Invalid(val nonexistentKey: String) : YamlTestSchema()

    /**
     * Test some basic value types
     */
    @Serializable
    data class Test1(val value1: String, val value2: Boolean, val value3: Int) : YamlTestSchema() {
        companion object {
            val EXPECTED_VALUE = Test1("value1", true, 123)
        }
    }

    /**
     * Test a list
     */
    @Serializable
    data class Test2(val valueList: List<String>) : YamlTestSchema() {
        companion object {
            val EXPECTED_VALUE = Test2(listOf("value1", "value2", "value3"))
        }
    }

    /**
     * Test nested objects
     */
    @Serializable
    data class Test3(val key1: Test1, val key2: List<Test1>, val key3: Test2) : YamlTestSchema() {
        companion object {
            val EXPECTED_VALUE = Test3(
                Test1("value1", true, 123),
                listOf(Test1("value1", true, 123), Test1("value2", false, 456)),
                Test2(listOf("value1", "value2", "value3"))
            )

        }
    }
}
