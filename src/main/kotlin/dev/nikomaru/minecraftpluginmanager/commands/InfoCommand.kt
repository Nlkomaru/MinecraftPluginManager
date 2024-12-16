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
import org.incendo.cloud.annotations.Argument
import org.incendo.cloud.annotations.Command
import org.incendo.cloud.annotations.Permission
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.File
import java.io.ObjectInput

@Command("mpm")
@Permission("mpm.command")
class InfoCommand : KoinComponent {
    private val plugin: MinecraftPluginManager by inject()

    @Command("info <plugin>")
    fun info(sender: CommandSender, @Argument("plugin") pluginName: PluginName) {
        val name = pluginName.name
        val folder = plugin.dataFolder.parentFile
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
        val message: String = pluginToMessage(pluginData)
        val divider = "-".repeat(10)
        sender.sendRichMessage("$divider\n$message\n$divider")
    }

    @Command("jarinfo <file>")
    fun jarInfo(sender: CommandSender, @Argument("file") file: File) {
        println(file)
        val data = PluginDataUtils.getPluginData(file)
        val message: String = pluginToMessage(data)
        val divider = "-".repeat(10)
        sender.sendRichMessage("$divider\n$message\n$divider")
    }


    private fun pluginToMessage(data: PluginData?) = when (data) {
        is PluginData.BukkitPluginData -> {
            """
        ${addColor("プラグイン名", data.name)}
        ${addColor("バージョン", data.version)}
        ${addColor("メインクラス", data.main)}
        ${addColor("説明", data.description)}
        ${addColor("作者", data.author)}
        ${addColor("ウェブサイト", data.website)}
        ${addColor("APIバージョン", data.apiVersion)}
        """.trimIndent()
        }

        is PluginData.PaperPluginData -> {
            """
        ${addColor("プラグイン名", data.name)}
        ${addColor("バージョン", data.version)}
        ${addColor("メインクラス", data.main)}
        ${addColor("説明", data.description)}
        ${addColor("APIバージョン", data.apiVersion)}
        ${addColor("ブートストラッパー", data.bootstrapper)}
        ${addColor("ローダー", data.loader)}
        ${addColor("作者", data.author)}
        ${addColor("ウェブサイト", data.website)}
        """.trimIndent()
        }

        else -> {
            "プラグインの情報を取得できませんでした。"
        }
    }

    fun addColor(tag: String, message: Any): String {
        return "<yellow>$tag<reset>: ${message.toString().replace("\n","")}"
    }
}
