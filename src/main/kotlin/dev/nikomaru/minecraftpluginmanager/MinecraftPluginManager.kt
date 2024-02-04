/*
 * Written in 2023-2024 by Nikomaru <nikomaru@nikomaru.dev>
 *
 * To the extent possible under law, the author(s) have dedicated all copyright and related and neighboring rights to this software to the public domain worldwide.This software is distributed without any warranty.
 *
 * You should have received a copy of the CC0 Public Domain Dedication along with this software.
 * If not, see <http://creativecommons.org/publicdomain/zero/1.0/>.
 */

package dev.nikomaru.minecraftpluginmanager

import dev.nikomaru.minecraftpluginmanager.commands.*
import org.bukkit.plugin.java.JavaPlugin
import org.koin.core.context.GlobalContext
import org.koin.dsl.module
import revxrsal.commands.bukkit.BukkitCommandHandler
import revxrsal.commands.ktx.supportSuspendFunctions

open class MinecraftPluginManager: JavaPlugin() {
    override fun onEnable() { // Plugin startup logic
        setCommand()
        setupKoin()
    }

    private fun setupKoin() {
        val appModule = module {
            single<MinecraftPluginManager> { this@MinecraftPluginManager }
        }

        GlobalContext.getOrNull() ?: GlobalContext.startKoin {
            modules(appModule)
        }
    }

    override fun onDisable() { // Plugin shutdown logic
    }

    private fun setCommand() {
        val handler = BukkitCommandHandler.create(this)

        handler.setSwitchPrefix("--")
        handler.setFlagPrefix("--")
        handler.supportSuspendFunctions()
        handler.enableAdventure()

        handler.setHelpWriter { command, _ ->
            java.lang.String.format(
                """
                <color:yellow>コマンド: <color:gray>%s %s
                <color:yellow>説明: <color:gray>%s
                
                """.trimIndent(),
                command.path.toRealString(),
                command.usage,
                command.description,
            )
        }

        with(handler) {
            register(
                HelpCommand(),
                InfoCommand(),
                InstallCommand(),
                ListCommand(),
                LockCommand(),
                OutdatedCommand(),
                ReloadCommand(),
                RemoveUnmanaged(),
                SearchCommand(),
                UpdateCommand(),
                VersionCommand()
            )
        }
    }
}