package dev.nikomaru.minecraftpluginmanager

import dev.nikomaru.minecraftpluginmanager.commands.*
import org.bukkit.plugin.java.JavaPlugin
import org.koin.core.context.GlobalContext
import org.koin.dsl.module
import revxrsal.commands.bukkit.BukkitCommandHandler
import revxrsal.commands.ktx.supportSuspendFunctions

open class MinecraftPluginManager : JavaPlugin() {

    override fun onEnable() {
        // Plugin startup logic
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

    override fun onDisable() {
        // Plugin shutdown logic
    }

    private fun setCommand() {

        val handler = BukkitCommandHandler.create(this)

        handler.setSwitchPrefix("--")
        handler.setFlagPrefix("--")
        handler.supportSuspendFunctions()

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
            register(HelpCommand())
            register(InfoCommand())
            register(InstallCommand())
            register(ListCommand())
            register(LockCommand())
            register(OutdatedCommand())
            register(ReloadCommand())
            register(RemoveUnmanaged())
            register(SearchCommand())
            register(UpdateCommand())
            register(VersionCommand())
        }


    }
}