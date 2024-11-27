/*
 * Written in 2023-2024 by Nikomaru <nikomaru@nikomaru.dev>
 *
 * To the extent possible under law, the author(s) have dedicated all copyright and related and neighboring rights to this software to the public domain worldwide.This software is distributed without any warranty.
 *
 * You should have received a copy of the CC0 Public Domain Dedication along with this software.
 * If not, see <http://creativecommons.org/publicdomain/zero/1.0/>.
 */

package dev.nikomaru.minecraftpluginmanager.repository.downloader.utils

import dev.nikomaru.minecraftpluginmanager.MinecraftPluginManager
import dev.nikomaru.minecraftpluginmanager.data.ManageData
import dev.nikomaru.minecraftpluginmanager.data.PluginData
import dev.nikomaru.minecraftpluginmanager.utils.PluginDataUtils
import dev.nikomaru.minecraftpluginmanager.utils.Utils.json
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.encodeToString
import org.apache.commons.io.FileUtils
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.File
import java.net.URL


object DownloaderUtils: KoinComponent {
    private val plugin: MinecraftPluginManager by inject()
    val client = HttpClient(CIO) {
        expectSuccess = false
        install(ContentNegotiation) {
            json(json = json)
        }
    }

    fun download(url: String, file: File, manageData: ManageData) {
        FileUtils.copyURLToFile(URL(url), file, 10 * 1000, 30 * 1000)
        val pluginData = PluginDataUtils.getPluginData(file)
        val identity: String
        when (pluginData) {
            is PluginData.PaperPluginData -> {
                plugin.logger.info("Paper plugin found")
                identity = pluginData.name
            }

            is PluginData.BukkitPluginData -> {
                plugin.logger.info("Bukkit plugin found")
                identity = pluginData.name
            }

            else -> {
                plugin.logger.info("Unknown plugin type")
                file.delete()
                plugin.logger.info("Deleted file")
                return
            }
        }
        val replacedData = manageData.copy(identify = identity)
        val manageFile = plugin.dataFolder.resolve("managed").resolve("$identity.json")
        manageFile.parentFile.mkdirs()
        val data = json.encodeToString(replacedData)
        manageFile.writeText(data)
    }
}