/*
 * Written in 2023-2024 by Nikomaru <nikomaru@nikomaru.dev>
 *
 * To the extent possible under law, the author(s) have dedicated all copyright and related and neighboring rights to this software to the public domain worldwide.This software is distributed without any warranty.
 *
 * You should have received a copy of the CC0 Public Domain Dedication along with this software.
 * If not, see <http://creativecommons.org/publicdomain/zero/1.0/>.
 */

package dev.nikomaru.minecraftpluginmanager.commands

import dev.nikomaru.minecraftpluginmanager.MinecraftPluginManager
import dev.nikomaru.minecraftpluginmanager.utils.PluginDataUtils.getPluginData
import dev.nikomaru.minecraftpluginmanager.value.PluginName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.plugin.Plugin
import org.incendo.cloud.annotations.Argument
import org.incendo.cloud.annotations.Command
import org.incendo.cloud.annotations.Permission
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.File
import java.util.stream.Collectors



//TODO : アンロードしたときにコマンド一覧空も削除

@Command("mpm")
@Permission("mpm.command.load")
class LoadCommand : KoinComponent {

    val plugin: MinecraftPluginManager by inject()

    @Command("load <file>")
    suspend fun load(sender: CommandSender, @Argument("file") file: File) {
        val data = getPluginData(file)
        val name =
            data?.getPluginName() ?: run { sender.sendRichMessage("<red>プラグインの読み込みに失敗しました。"); return }
        plugin.server.pluginManager.plugins.find { it.name == name }?.let {
            if(it.isEnabled){
                sender.sendRichMessage("<red>プラグインは既に読み込まれています。<br> /mpm unload $name または /mpm reload $name で再読み込みしてください。")
                return
            }
        }
        val newTarget = plugin.server.pluginManager.loadPlugin(file) ?: run { sender.sendRichMessage("<red>プラグインの読み込みに失敗しました。"); return }
        newTarget.onLoad()
        plugin.server.pluginManager.enablePlugin(newTarget)

        delay(1000)


        sender.sendRichMessage("<green>プラグインを読み込みました。")
    }

    @Command("unload <plugin>")
    fun unload(sender: CommandSender, @Argument("plugin") pluginName: PluginName) {
        val name = pluginName.name
        val target = plugin.server.pluginManager.getPlugin(name)
            ?: run { sender.sendRichMessage("<red>プラグインが見つかりませんでした。"); return }
        plugin.server.pluginManager.disablePlugin(target)
        sender.sendRichMessage("<green>プラグインをアンロードしました。")
    }

    @Command("reload <plugin>")
    suspend fun reload(sender: CommandSender, @Argument("plugin") pluginName: PluginName) {
        val name = pluginName.name
        val target = plugin.server.pluginManager.getPlugin(name)
            ?: run { sender.sendRichMessage("<red>プラグインが見つかりませんでした。"); return }
        Bukkit.getPluginManager().disablePlugin(target)
        // プラグインが無効化されるまで待機
        sender.sendRichMessage("<green>プラグインをアンロードしました。")

        // プラグインを再読み込み
        sender.sendRichMessage("<green>プラグインを再読み込みしています...")
        delay(1000)

        val file: File = withContext(Dispatchers.IO) {
            plugin.dataFolder.parentFile.listFiles()?.filter{ it.name.endsWith(".jar") }?.find { getPluginData(it)?.getPluginName() == name }
                ?: return@withContext null
        } ?: run { sender.sendRichMessage("<red>プラグインが見つかりませんでした。"); return }

        val newTarget = plugin.server.pluginManager.loadPlugin(file) ?: run { sender.sendRichMessage("<red>プラグインの読み込みに失敗しました。"); return }
        newTarget.onLoad()
        plugin.server.pluginManager.enablePlugin(newTarget)
        sender.sendRichMessage("<green>プラグインを再読み込みしました。")
    }


    private fun getCommandsFromPlugin(plugin: Plugin): List<Map.Entry<String, Command>> {
        val knownCommands: Map<String, org.bukkit.command.Command> = this.getKnownCommands()
        return knownCommands.entries.stream()
            .filter { s: Map.Entry<String, org.bukkit.command.Command> ->
                if (s.key.contains(":")) return@filter s.key.split(":".toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray()[0].equals(plugin.name, ignoreCase = true)
                else {
                    val cl = s.value.javaClass.classLoader
                    try {
                        return@filter cl.javaClass == this.pluginClassLoader && this.pluginClassLoaderPlugin.get(cl) === plugin
                    } catch (e: IllegalAccessException) {
                        return@filter false
                    }
                }
            }
            .collect(Collectors.toList<Map.Entry<String, Command>>())
    }
}