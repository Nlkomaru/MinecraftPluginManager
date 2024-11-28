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
import dev.nikomaru.minecraftpluginmanager.commands.utils.parser.FileParser
import dev.nikomaru.minecraftpluginmanager.commands.utils.parser.PluginNameParser
import org.bukkit.command.CommandSender
import org.bukkit.command.defaults.HelpCommand
import org.bukkit.plugin.java.JavaPlugin
import org.incendo.cloud.annotations.AnnotationParser
import org.incendo.cloud.execution.ExecutionCoordinator
import org.incendo.cloud.kotlin.coroutines.annotations.installCoroutineSupport
import org.incendo.cloud.paper.LegacyPaperCommandManager
import org.incendo.cloud.setting.ManagerSetting
import org.koin.core.context.GlobalContext
import org.koin.dsl.module


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
        val commandManager = LegacyPaperCommandManager.createNative(
                this,
        ExecutionCoordinator.simpleCoordinator()
        )


        commandManager.settings().set(ManagerSetting.ALLOW_UNSAFE_REGISTRATION, true)

        commandManager.parserRegistry().registerParser(FileParser.fileParser())
        commandManager.parserRegistry().registerParser(PluginNameParser.pluginNameParser())

        val annotationParser = AnnotationParser(commandManager, CommandSender::class.java)
        annotationParser.installCoroutineSupport()

        with(annotationParser) {
            parse(
                HelpCommand(),
                InfoCommand(),
                InstallCommand(),
                ListCommand(),
                LockCommand(),
                OutdatedCommand(),
                LoadCommand(),
                RemoveUnmanagedCommand(),
                SearchCommand(),
                UpdateCommand(),
                VersionCommand()
            )
        }
    }
}