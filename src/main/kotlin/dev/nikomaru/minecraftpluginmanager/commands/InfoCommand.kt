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
import dev.nikomaru.minecraftpluginmanager.data.PluginData
import dev.nikomaru.minecraftpluginmanager.utils.PluginDataUtils
import dev.nikomaru.minecraftpluginmanager.value.PluginName
import org.bukkit.command.CommandSender
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import revxrsal.commands.annotation.Command
import revxrsal.commands.annotation.Subcommand
import revxrsal.commands.bukkit.annotation.CommandPermission

@Command("mpm")
@CommandPermission("mpm.command")
class InfoCommand: KoinComponent {
    private val plugin: MinecraftPluginManager by inject()

    @Subcommand("info")
    fun info(actor: CommandSender, pluginName: PluginName) {
        val name = pluginName.name
        val folder = plugin.dataFolder
        val pluginFile = folder.listFiles()?.filter { it.name.endsWith(".jar") }?.find {
            when (val data = PluginDataUtils.getPluginData(it)) {
                is PluginData.BukkitPluginData -> {
                    data.name == name
                }

                is PluginData.PaperPluginData -> {
                    data.name == name
                }

                else -> {
                    false
                }
            }
        }
        val pluginData = pluginFile?.let { PluginDataUtils.getPluginData(it) }
        val message: String = when (pluginData) {
            is PluginData.BukkitPluginData -> {
                """
                    プラグイン名: ${pluginData.name}
                    バージョン: ${pluginData.version}
                    メインクラス: ${pluginData.main}
                    説明: ${pluginData.description}
                    作者: ${pluginData.author}
                    ウェブサイト: ${pluginData.website}
                    APIバージョン: ${pluginData.apiVersion}
                """.trimIndent()
            }

            is PluginData.PaperPluginData -> {
                """
                    プラグイン名: ${pluginData.name}
                    バージョン: ${pluginData.version}
                    メインクラス: ${pluginData.main}
                    説明: ${pluginData.description}
                    APIバージョン: ${pluginData.apiVersion}
                    ブートストラッパー: ${pluginData.bootstrapper}
                    ローダー: ${pluginData.loader}
                    作者: ${pluginData.author}
                    ウェブサイト: ${pluginData.website}
                """.trimIndent()
            }

            else -> {
                "プラグインの情報を取得できませんでした。"
            }
        }
        actor.sendRichMessage(message)
    }
}