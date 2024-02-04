/*
 * Written in 2023-2024 by Nikomaru <nikomaru@nikomaru.dev>
 *
 * To the extent possible under law, the author(s) have dedicated all copyright and related and neighboring rights to this software to the public domain worldwide.This software is distributed without any warranty.
 *
 * You should have received a copy of the CC0 Public Domain Dedication along with this software.
 * If not, see <http://creativecommons.org/publicdomain/zero/1.0/>.
 */

package dev.nikomaru.minecraftpluginmanager.commands

import be.seeseemelk.mockbukkit.ServerMock
import dev.nikomaru.minecraftpluginmanager.MinecraftPluginManagerTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.koin.test.KoinTest
import org.koin.test.inject
import java.util.*

@ExtendWith(MinecraftPluginManagerTest::class)
class VersionCommandTest : KoinTest {
    private val server: ServerMock by inject()

    @Test
    @DisplayName("command test: show plugin version")
    fun sendVersion() {
        val player = server.addPlayer(UUID.randomUUID().toString())
        player.isOp = true
        val result = player.performCommand("mpm version")
        println(player.nextMessage())
        assert(result)
    }
}