package xyz.rose.gateway.core.config

import kotlinx.serialization.builtins.serializer
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.deleteIfExists
import kotlin.io.path.exists
import kotlin.io.path.readText
import kotlin.io.path.writeBytes
import kotlin.io.path.writeText
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

/**
 * Tests for [YamlFileConfigSource]
 */
class YamlFileConfigSourceTest {
    /**
     * A temporary directory to use for testing file I/O operations
     */
    @TempDir
    lateinit var tempDir: Path

    /**
     * Gets a YAML file config source for a given file.
     *
     * @param T The type of the config object
     * @param file The name of the file to load
     */
    fun <T> getSource(file: String) = YamlFileConfigSource<T>(configPath(file))

    /**
     * The path to the config file
     *
     * @param file The file name
     */
    fun configPath(file: String): Path = tempDir.resolve("config/$file")

    @BeforeEach
    fun setup() {
        // Ensure the config directory exists in temp
        tempDir.resolve("config").createDirectories()

        // Seed resources from src/test/resources into the temp directory
        listOf("test1.yml", "test2.yml", "test3.yml", "invalid.yml").forEach { fileName ->
            val resource = this::class.java.classLoader.getResourceAsStream("config/$fileName")
            if (resource != null) {
                val target = configPath(fileName)
                target.writeBytes(resource.readAllBytes())
            }
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

            assertIs<GatewayConfigLoadResult.Uninitialized<String>>(result, "load result should be uninitialized")
        }

        @Test
        fun `source fails load with error result when file is invalid`() {
            val result = getSource<YamlTestSchema.Invalid>("invalid.yml").load(YamlTestSchema.Invalid.serializer())

            assertIs<GatewayConfigLoadResult.Failure<YamlTestSchema.Invalid>>(result, "load result should be failure")
        }

        @Test
        fun `source loads yaml schema test 1`() {
            val result = getSource<YamlTestSchema.Test1>("test1.yml").load(YamlTestSchema.Test1.serializer())

            assertIs<GatewayConfigLoadResult.Success<YamlTestSchema.Test1>>(result, "load result should be success")
            assertEquals(YamlTestSchema.Test1.EXPECTED_VALUE, result.data, "loaded data should match test 1 schema")
        }

        @Test
        fun `source loads yaml schema test 2`() {
            val result = getSource<YamlTestSchema.Test2>("test2.yml").load(YamlTestSchema.Test2.serializer())

            assertIs<GatewayConfigLoadResult.Success<YamlTestSchema.Test2>>(result, "load result should be success")
            assertEquals(YamlTestSchema.Test2.EXPECTED_VALUE, result.data, "loaded data should match test 2 schema")
        }

        @Test
        fun `source loads yaml schema test3`() {
            val result = getSource<YamlTestSchema.Test3>("test3.yml").load(YamlTestSchema.Test3.serializer())

            assertIs<GatewayConfigLoadResult.Success<YamlTestSchema.Test3>>(result, "load result should be success")
            assertEquals(YamlTestSchema.Test3.EXPECTED_VALUE, result.data, "loaded data should match test 3 schema")
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

            assertIs<GatewayConfigSaveResult.Failure>(result, "save result should be failure")
            assertFalse(configPath(filePath).exists(), "file should not exist")
        }

        @Test
        fun `source succeeds save when file can be created on path`() {
            val filePath = "test_save.yml"
            configPath(filePath).deleteIfExists()
            val result = getSource<String>(filePath).save(String.serializer(), "hello")

            assertIs<GatewayConfigSaveResult.Success>(result, "save result should be success")
            assertTrue(configPath(filePath).exists(), "file should exist")
        }

        @Test
        fun `source overwrites existing file on path`() {
            val filePath = "test_save.yml"
            val file = configPath(filePath)
            file.deleteIfExists()
            file.writeText("old data")
            val result = getSource<String>(filePath).save(String.serializer(), "hello")

            assertIs<GatewayConfigSaveResult.Success>(result, "save result should be success")
            assertTrue(configPath(filePath).exists(), "file should exist")
            assertEquals("hello", file.readText(), "file content should be updated")
        }

        @Test
        fun `source saves yaml schema test 1`() {
            val fileName = "test1_save.yml"
            configPath(fileName).deleteIfExists()
            val result = getSource<YamlTestSchema.Test1>(fileName).save(
                YamlTestSchema.Test1.serializer(),
                YamlTestSchema.Test1.EXPECTED_VALUE
            )

            assertIs<GatewayConfigSaveResult.Success>(result, "save result should be success")

            val savedFile = getSource<YamlTestSchema.Test1>(fileName).load(YamlTestSchema.Test1.serializer())

            assertIs<GatewayConfigLoadResult.Success<YamlTestSchema.Test1>>(
                savedFile,
                "loading saved file should be success"
            )
            assertEquals(
                YamlTestSchema.Test1.EXPECTED_VALUE,
                savedFile.data,
                "saved file should not differ after reload"
            )
        }

        @Test
        fun `source saves yaml schema test 2`() {
            val fileName = "test2_save.yml"
            configPath(fileName).deleteIfExists()
            val result = getSource<YamlTestSchema.Test2>(fileName).save(
                YamlTestSchema.Test2.serializer(),
                YamlTestSchema.Test2.EXPECTED_VALUE
            )

            assertIs<GatewayConfigSaveResult.Success>(result, "save result should be success")

            val savedFile = getSource<YamlTestSchema.Test2>(fileName).load(YamlTestSchema.Test2.serializer())

            assertIs<GatewayConfigLoadResult.Success<YamlTestSchema.Test2>>(
                savedFile,
                "loading saved file should be success"
            )
            assertEquals(
                YamlTestSchema.Test2.EXPECTED_VALUE,
                savedFile.data,
                "saved file should match test 2 schema"
            )
        }

        @Test
        fun `source saves yaml schema test 3`() {
            val fileName = "test3_save.yml"
            configPath(fileName).deleteIfExists()
            val result = getSource<YamlTestSchema.Test3>(fileName).save(
                YamlTestSchema.Test3.serializer(),
                YamlTestSchema.Test3.EXPECTED_VALUE
            )

            assertIs<GatewayConfigSaveResult.Success>(result, "save result should be success")

            val savedFile = getSource<YamlTestSchema.Test3>(fileName).load(YamlTestSchema.Test3.serializer())

            assertIs<GatewayConfigLoadResult.Success<YamlTestSchema.Test3>>(
                savedFile,
                "loading saved file should be success"
            )
            assertEquals(
                YamlTestSchema.Test3.EXPECTED_VALUE,
                savedFile.data,
                "saved file should match test 3 schema"
            )
        }
    }
}
