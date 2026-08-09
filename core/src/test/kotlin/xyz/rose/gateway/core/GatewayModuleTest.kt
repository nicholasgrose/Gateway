package xyz.rose.gateway.core

import io.github.oshai.kotlinlogging.KLogger
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.AfterEach
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import xyz.rose.gateway.core.config.ConfigSchema
import xyz.rose.gateway.core.config.ConfigSource
import xyz.rose.gateway.core.platform.GatewayPlatformDefinition
import xyz.rose.gateway.core.platform.PlatformProvider
import kotlin.test.Test
import kotlin.test.assertNotNull

/**
 * Tests for [gatewayModule]
 */
class GatewayModuleTest : KoinTest {

    @AfterEach
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `test gatewayModule definitions`() {
        val platformProvider = mockk<PlatformProvider>()
        val schema = mockk<ConfigSchema<Any>>(relaxed = true)

        // Use a simple function instead of Scope extension to avoid Koin trying to inject Scope
        val platformDefinition = GatewayPlatformDefinition("test", "t1", schema) { platformProvider }

        val logger = mockk<KLogger>(relaxed = true)
        val configSource = mockk<ConfigSource<*>>(relaxed = true)

        val env = mockk<GatewayEnvironment>(relaxed = true) {
            every { this@mockk.logger } returns logger
            every { platforms } returns listOf(platformDefinition)
            every { source } returns configSource
            every { configProvider } returns { mockk(relaxed = true) }
            every { appProvider } returns { mockk(relaxed = true) }
        }

        startKoin {
            modules(gatewayModule(env))
        }

        // Check if basic definitions are present
        assertNotNull(getKoin().get<KLogger>())
        assertNotNull(getKoin().get<Collection<GatewayPlatformDefinition<*>>>())
    }
}
