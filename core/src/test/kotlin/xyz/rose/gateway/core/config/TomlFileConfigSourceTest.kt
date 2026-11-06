package xyz.rose.gateway.core.config

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.createFile
import kotlin.io.path.deleteIfExists
import kotlin.io.path.exists
import kotlin.io.path.writeBytes
import kotlin.io.path.writeText
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

/**
 * Tests for [TomlFileConfigSource]
 */
class TomlFileConfigSourceTest {
    /**
     * A temporary directory to use for testing file I/O operations
     */
    @TempDir
    lateinit var tempDir: Path

    private val configDir get() = tempDir.resolve("config")

    /**
     * Gets a TOML file config source for a given file.
     *
     * @param T The type of the config object
     * @param file The name of the file to load
     */
    private fun <T> getSource(file: String) = TomlFileConfigSource<T>(configPath(file))

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
        listOf("test1.toml", "test2.toml", "test3.toml", "invalid.toml").forEach { fileName ->
            val resource = this::class.java.classLoader.getResourceAsStream("config/$fileName")
            resource?.use { configPath(fileName).writeBytes(it.readAllBytes()) }
        }
    }

    /**
     * Tests for the TOML load logic
     */
    @Nested
    inner class TomlFileConfigSourceLoadTests {
        @Test
        fun `source fails load with uninitialized result when file doesn't exist`() {
            val result = getSource<TomlTestSchema.Test1>("does_not_exist.toml").load(TomlTestSchema.Test1.serializer())

            assertIs<GatewayConfigLoadResult.Uninitialized<TomlTestSchema.Test1>>(result)
        }

        @Test
        fun `source fails load with error result when file is invalid`() {
            val result = getSource<TomlTestSchema.Invalid>("invalid.toml").load(TomlTestSchema.Invalid.serializer())

            assertIs<GatewayConfigLoadResult.Failure<TomlTestSchema.Invalid>>(result)
        }

        @Test
        fun `source loads toml schema test 1`() {
            val result = getSource<TomlTestSchema.Test1>("test1.toml").load(TomlTestSchema.Test1.serializer())

            assertIs<GatewayConfigLoadResult.Success<TomlTestSchema.Test1>>(result)
            assertEquals(TomlTestSchema.Test1.EXPECTED_VALUE, result.data)
        }

        @Test
        fun `source loads toml schema test 2`() {
            val result = getSource<TomlTestSchema.Test2>("test2.toml").load(TomlTestSchema.Test2.serializer())

            assertIs<GatewayConfigLoadResult.Success<TomlTestSchema.Test2>>(result)
            assertEquals(TomlTestSchema.Test2.EXPECTED_VALUE, result.data)
        }

        @Test
        fun `source loads toml schema test 3`() {
            val result = getSource<TomlTestSchema.Test3>("test3.toml").load(TomlTestSchema.Test3.serializer())

            assertIs<GatewayConfigLoadResult.Success<TomlTestSchema.Test3>>(result)
            assertEquals(TomlTestSchema.Test3.EXPECTED_VALUE, result.data)
        }
    }

    /**
     * Tests for the TOML save logic
     */
    @Nested
    inner class TomlFileConfigSourceSaveTests {
        @Test
        fun `source fails save with error result when file path is invalid`() {
            val dirAsFile = configPath("not_a_dir")
            dirAsFile.createFile()
            val filePath = "not_a_dir/bad.toml"
            val result = getSource<TomlTestSchema.Test1>(filePath).save(
                TomlTestSchema.Test1.serializer(),
                TomlTestSchema.Test1.EXPECTED_VALUE
            )

            assertIs<GatewayConfigSaveResult.Failure>(result)
        }

        @Test
        fun `source succeeds save when file can be created on path`() {
            val filePath = "test_save.toml"
            configPath(filePath).deleteIfExists()
            val result = getSource<TomlTestSchema.Test1>(filePath).save(
                TomlTestSchema.Test1.serializer(),
                TomlTestSchema.Test1.EXPECTED_VALUE
            )

            assertIs<GatewayConfigSaveResult.Success>(result)
            assertTrue(configPath(filePath).exists())
        }

        @Test
        fun `source overwrites existing file on path`() {
            val filePath = "test_save.toml"
            val file = configPath(filePath)
            file.deleteIfExists()
            file.writeText("old data")
            val result = getSource<TomlTestSchema.Test1>(filePath).save(
                TomlTestSchema.Test1.serializer(),
                TomlTestSchema.Test1.EXPECTED_VALUE
            )

            assertIs<GatewayConfigSaveResult.Success>(result)
            assertTrue(configPath(filePath).exists())
            val savedFile = getSource<TomlTestSchema.Test1>(filePath).load(TomlTestSchema.Test1.serializer())
            assertIs<GatewayConfigLoadResult.Success<TomlTestSchema.Test1>>(savedFile)
            assertEquals(TomlTestSchema.Test1.EXPECTED_VALUE, savedFile.data)
        }

        @Test
        fun `source saves toml schema test 1`() {
            val fileName = "test1_save.toml"
            configPath(fileName).deleteIfExists()
            val result = getSource<TomlTestSchema.Test1>(fileName).save(
                TomlTestSchema.Test1.serializer(),
                TomlTestSchema.Test1.EXPECTED_VALUE
            )

            assertIs<GatewayConfigSaveResult.Success>(result)

            val savedFile = getSource<TomlTestSchema.Test1>(fileName).load(TomlTestSchema.Test1.serializer())

            assertIs<GatewayConfigLoadResult.Success<TomlTestSchema.Test1>>(savedFile)
            assertEquals(TomlTestSchema.Test1.EXPECTED_VALUE, savedFile.data)
        }

        @Test
        fun `source saves toml schema test 2`() {
            val fileName = "test2_save.toml"
            configPath(fileName).deleteIfExists()
            val result = getSource<TomlTestSchema.Test2>(fileName).save(
                TomlTestSchema.Test2.serializer(),
                TomlTestSchema.Test2.EXPECTED_VALUE
            )

            assertIs<GatewayConfigSaveResult.Success>(result)

            val savedFile = getSource<TomlTestSchema.Test2>(fileName).load(TomlTestSchema.Test2.serializer())

            assertIs<GatewayConfigLoadResult.Success<TomlTestSchema.Test2>>(savedFile)
            assertEquals(TomlTestSchema.Test2.EXPECTED_VALUE, savedFile.data)
        }

        @Test
        fun `source saves toml schema test 3`() {
            val fileName = "test3_save.toml"
            configPath(fileName).deleteIfExists()
            val result = getSource<TomlTestSchema.Test3>(fileName).save(
                TomlTestSchema.Test3.serializer(),
                TomlTestSchema.Test3.EXPECTED_VALUE
            )

            assertIs<GatewayConfigSaveResult.Success>(result)

            val savedFile = getSource<TomlTestSchema.Test3>(fileName).load(TomlTestSchema.Test3.serializer())

            assertIs<GatewayConfigLoadResult.Success<TomlTestSchema.Test3>>(savedFile)
            assertEquals(TomlTestSchema.Test3.EXPECTED_VALUE, savedFile.data)
        }
    }
}
