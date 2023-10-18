package dev.nikomaru.minecraftpluginmanager

import dev.nikomaru.minecraftpluginmanager.commands.Command
import dev.nikomaru.minecraftpluginmanager.commands.Command.Companion.jsonFormant
import dev.nikomaru.minecraftpluginmanager.commands.Suggestion
import dev.nikomaru.minecraftpluginmanager.data.ManageList
import kotlinx.serialization.decodeFromString
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import revxrsal.commands.autocomplete.SuggestionProvider
import revxrsal.commands.bukkit.BukkitCommandHandler
import revxrsal.commands.command.CommandActor
import revxrsal.commands.command.CommandParameter
import revxrsal.commands.command.ExecutableCommand
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

        handler.autoCompleter.registerSuggestionFactory { parameter: CommandParameter ->
            if (parameter.hasAnnotation(Suggestion::class.java)) {
                val string = parameter.getAnnotation(Suggestion::class.java).value
                if (string == "identify") {
                    return@registerSuggestionFactory SuggestionProvider { _: List<String>, _: CommandActor, _: ExecutableCommand ->
                        val file = dataFolder.resolve("manageList.json")
                        val list = jsonFormant.decodeFromString<ManageList>(file.readText())
                        if (file.exists()) {
                            return@SuggestionProvider list.list.map { it.identify }
                        }
                        return@SuggestionProvider arrayListOf<String>()
                    }
                }
                return@registerSuggestionFactory null
            }
            null
        }

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
            register(Command())
        }


    }
}