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
class InfoCommand : KoinComponent {
    private val plugin: MinecraftPluginManager by inject()

    @Subcommand("info")
    fun info(actor: CommandSender, pluginName: PluginName) {
        val name = pluginName.name
        val folder = plugin.dataFolder
        val pluginFile = folder.listFiles()?.filter { it.name.endsWith(".jar") }?.find {
            val data = PluginDataUtils.getPluginData(it)
            if (data is PluginData.BukkitPluginData) {
                data.name == name
            } else if (data is PluginData.PaperPluginData) {
                data.name == name
            } else {
                false
            }
        }
        val pluginData = pluginFile?.let { PluginDataUtils.getPluginData(it) }
        val message: String = if (pluginData is PluginData.BukkitPluginData) {
            """
                プラグイン名: ${pluginData.name}
                バージョン: ${pluginData.version}
                メインクラス: ${pluginData.main}
                説明: ${pluginData.description}
                作者: ${pluginData.author}
                ウェブサイト: ${pluginData.website}
                APIバージョン: ${pluginData.apiVersion}
            """.trimIndent()
        } else if (pluginData is PluginData.PaperPluginData) {
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
        } else {
            "プラグインの情報を取得できませんでした。"
        }
        actor.sendRichMessage(message)
    }
}