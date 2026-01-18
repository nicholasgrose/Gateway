package xyz.rose.gateway.core.config

import kotlinx.serialization.builtins.serializer
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Path
import kotlin.io.path.*
import kotlin.test.*

/**
 * Tests for [YamlFileConfigSource]
 */
class YamlFileConfigSourceTest {
    /**
     * A temporary directory to use for testing file I/O operations
     */
    @TempDir
    lateinit var tempDir: Path

    private val configDir get() = tempDir.resolve("config")

    /**
     * Gets a YAML file config source for a given file.
     *
     * @param T The type of the config object
     * @param file The name of the file to load
     */
    private fun <T> getSource(file: String) = YamlFileConfigSource<T>(configPath(file))

    /**
     * The path to the config file
     *
     * @param file The file name
     */
    private fun configPath(file: String): Path = configDir.resolve(file)

    @BeforeEach
    fun setup() {
        // Ensure the config directory exists in temp
        configDir.createDirectories()

        // Seed resources from src/test/resources into the temp directory
        listOf("test1.yml", "test2.yml", "test3.yml", "invalid.yml").forEach { fileName ->
            val resource = this::class.java.classLoader.getResourceAsStream("config/$fileName")
            resource?.use { configPath(fileName).writeBytes(it.readAllBytes()) }
        }
    }

    /**
     * Tests for the YAML load logic
     */
    @Nested
    inner class YamlFileConfigSourceLoadTests {
        @Test
        fun `source fails load with uninitialized result when file doesn't exist`() {
            val result = getSource<String>("does_not_exist.yml").load(String.serializer())

            assertIs<GatewayConfigLoadResult.Uninitialized<String>>(result)
        }

        @Test
        fun `source fails load with error result when file is invalid`() {
            val result = getSource<YamlTestSchema.Invalid>("invalid.yml").load(YamlTestSchema.Invalid.serializer())

            assertIs<GatewayConfigLoadResult.Failure<YamlTestSchema.Invalid>>(result)
        }

        @Test
        fun `source loads yaml schema test 1`() {
            val result = getSource<YamlTestSchema.Test1>("test1.yml").load(YamlTestSchema.Test1.serializer())

            assertIs<GatewayConfigLoadResult.Success<YamlTestSchema.Test1>>(result)
            assertEquals(YamlTestSchema.Test1.EXPECTED_VALUE, result.data)
        }

        @Test
        fun `source loads yaml schema test 2`() {
            val result = getSource<YamlTestSchema.Test2>("test2.yml").load(YamlTestSchema.Test2.serializer())

            assertIs<GatewayConfigLoadResult.Success<YamlTestSchema.Test2>>(result)
            assertEquals(YamlTestSchema.Test2.EXPECTED_VALUE, result.data)
        }

        @Test
        fun `source loads yaml schema test 3`() {
            val result = getSource<YamlTestSchema.Test3>("test3.yml").load(YamlTestSchema.Test3.serializer())

            assertIs<GatewayConfigLoadResult.Success<YamlTestSchema.Test3>>(result)
            assertEquals(YamlTestSchema.Test3.EXPECTED_VALUE, result.data)
        }
    }

    /**
     * Tests for the YAML save logic
     */
    @Nested
    inner class YamlFileConfigSourceSaveTests {
        @Test
        fun `source fails save with error result when file path is invalid`() {
            val filePath = "does_not_exist/bad.yml"
            val result = getSource<String>(filePath).save(String.serializer(), "hello")

            assertIs<GatewayConfigSaveResult.Failure>(result)
            assertFalse(configPath(filePath).exists())
        }

        @Test
        fun `source succeeds save when file can be created on path`() {
            val filePath = "test_save.yml"
            configPath(filePath).deleteIfExists()
            val result = getSource<String>(filePath).save(String.serializer(), "hello")

            assertIs<GatewayConfigSaveResult.Success>(result)
            assertTrue(configPath(filePath).exists())
        }

        @Test
        fun `source overwrites existing file on path`() {
            val filePath = "test_save.yml"
            val file = configPath(filePath)
            file.deleteIfExists()
            file.writeText("old data")
            val result = getSource<String>(filePath).save(String.serializer(), "hello")

            assertIs<GatewayConfigSaveResult.Success>(result)
            assertTrue(configPath(filePath).exists())
            assertEquals("hello", file.readText())
        }

        @Test
        fun `source saves yaml schema test 1`() {
            val fileName = "test1_save.yml"
            configPath(fileName).deleteIfExists()
            val result = getSource<YamlTestSchema.Test1>(fileName).save(
                YamlTestSchema.Test1.serializer(),
                YamlTestSchema.Test1.EXPECTED_VALUE
            )

            assertIs<GatewayConfigSaveResult.Success>(result)

            val savedFile = getSource<YamlTestSchema.Test1>(fileName).load(YamlTestSchema.Test1.serializer())

            assertIs<GatewayConfigLoadResult.Success<YamlTestSchema.Test1>>(savedFile)
            assertEquals(YamlTestSchema.Test1.EXPECTED_VALUE, savedFile.data)
        }

        @Test
        fun `source saves yaml schema test 2`() {
            val fileName = "test2_save.yml"
            configPath(fileName).deleteIfExists()
            val result = getSource<YamlTestSchema.Test2>(fileName).save(
                YamlTestSchema.Test2.serializer(),
                YamlTestSchema.Test2.EXPECTED_VALUE
            )

            assertIs<GatewayConfigSaveResult.Success>(result)

            val savedFile = getSource<YamlTestSchema.Test2>(fileName).load(YamlTestSchema.Test2.serializer())

            assertIs<GatewayConfigLoadResult.Success<YamlTestSchema.Test2>>(savedFile)
            assertEquals(YamlTestSchema.Test2.EXPECTED_VALUE, savedFile.data)
        }

        @Test
        fun `source saves yaml schema test 3`() {
            val fileName = "test3_save.yml"
            configPath(fileName).deleteIfExists()
            val result = getSource<YamlTestSchema.Test3>(fileName).save(
                YamlTestSchema.Test3.serializer(),
                YamlTestSchema.Test3.EXPECTED_VALUE
            )

            assertIs<GatewayConfigSaveResult.Success>(result)

            val savedFile = getSource<YamlTestSchema.Test3>(fileName).load(YamlTestSchema.Test3.serializer())

            assertIs<GatewayConfigLoadResult.Success<YamlTestSchema.Test3>>(savedFile)
            assertEquals(YamlTestSchema.Test3.EXPECTED_VALUE, savedFile.data)
        }
    }
}
