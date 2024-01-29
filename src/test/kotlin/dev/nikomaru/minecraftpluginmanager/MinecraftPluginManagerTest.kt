package dev.nikomaru.minecraftpluginmanager

import be.seeseemelk.mockbukkit.MockBukkit
import be.seeseemelk.mockbukkit.ServerMock
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.koin.core.context.loadKoinModules
import org.koin.core.context.stopKoin
import org.koin.dsl.module

class MinecraftPluginManagerTest: BeforeEachCallback, AfterEachCallback {

    lateinit var server: ServerMock
    lateinit var plugin: MinecraftPluginManager

    override fun beforeEach(context: ExtensionContext) {
        println("beforeEach() executed before " + context.displayName + ".");
        server = MockBukkit.mock()
        setupKoin()
    }

    override fun afterEach(context: ExtensionContext) {
        MockBukkit.unmock()
        stopKoin()
    }


    private fun setupKoin() {
        plugin = MockBukkit.load(MinecraftPluginManager::class.java)
        val appModule = module {
            single<MinecraftPluginManager> { plugin }
            single<ServerMock> { server }
        }
        loadKoinModules(appModule)
    }

}