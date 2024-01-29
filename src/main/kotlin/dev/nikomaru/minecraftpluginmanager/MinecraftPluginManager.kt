package dev.nikomaru.minecraftpluginmanager

import dev.nikomaru.minecraftpluginmanager.commands.UpdateCommand
import dev.nikomaru.minecraftpluginmanager.commands.RemoveUnmanaged
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import revxrsal.commands.bukkit.BukkitCommandHandler
import revxrsal.commands.ktx.supportSuspendFunctions

class MinecraftPluginManager : JavaPlugin() {

    companion object {
        lateinit var plugin: Plugin
    }

    override fun onEnable() {
        // Plugin startup logic
        plugin = this
        setCommand()
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
            register(UpdateCommand())
            register(RemoveUnmanaged())
        }


    }
}