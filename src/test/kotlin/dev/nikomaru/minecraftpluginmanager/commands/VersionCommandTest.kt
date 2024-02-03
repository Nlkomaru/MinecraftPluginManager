package dev.nikomaru.minecraftpluginmanager.commands

import be.seeseemelk.mockbukkit.ServerMock
import dev.nikomaru.minecraftpluginmanager.MinecraftPluginManagerTest
import org.junit.jupiter.api.extension.ExtendWith
import org.koin.test.KoinTest
import org.koin.test.inject

@ExtendWith(MinecraftPluginManagerTest::class)
class VersionCommandTest: KoinTest {
    private val server : ServerMock by inject()

    @org.junit.jupiter.api.Test
    fun sendVersion() {
        val player = server.addPlayer()
        player.isOp = true
        val result = player.performCommand("mpm version")
        println(player.nextMessage())
        assert(result)
    }
}