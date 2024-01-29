package dev.nikomaru.minecraftpluginmanager.commands

import be.seeseemelk.mockbukkit.ServerMock
import dev.nikomaru.minecraftpluginmanager.MinecraftPluginManagerTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.extension.ExtendWith
import org.koin.test.KoinTest
import org.koin.test.inject
import org.junit.jupiter.api.Test
@ExtendWith(MinecraftPluginManagerTest::class)
class HelpCommandTest: KoinTest {
    private val server : ServerMock by inject()

    @Test
    @DisplayName("command test: plugin-template help")
    fun sendHelp(){
        val player = server.addPlayer()
        player.isOp = true
        val result  = player.performCommand("mpm help")
        println(player.nextMessage())
        assert(result)
    }
}