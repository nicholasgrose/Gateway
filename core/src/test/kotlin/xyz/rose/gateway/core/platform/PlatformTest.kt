package xyz.rose.gateway.core.platform

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.extension.RegisterExtension
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.get
import org.koin.test.inject
import org.koin.test.junit5.KoinTestExtension
import kotlin.test.Test

class ComponentA
class ComponentB(val a: ComponentA)

class PlatformTest : KoinTest {
    private val componentB by inject<ComponentB>()

    @JvmField
    @RegisterExtension
    val koinTestExtension = KoinTestExtension.create {
        modules(
            module {
                single { ComponentA() }
                single { ComponentB(get()) }
            })
    }

    @Test
    fun contextIsCreatedForTheTest() {
        Assertions.assertNotNull(get<ComponentA>())
        Assertions.assertNotNull(componentB)
    }
}
