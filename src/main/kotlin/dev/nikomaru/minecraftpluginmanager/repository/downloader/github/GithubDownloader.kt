/*
 * Written in 2023-2024 by Nikomaru <nikomaru@nikomaru.dev>
 *
 * To the extent possible under law, the author(s) have dedicated all copyright and related and neighboring rights to this software to the public domain worldwide.This software is distributed without any warranty.
 *
 * You should have received a copy of the CC0 Public Domain Dedication along with this software.
 * If not, see <http://creativecommons.org/publicdomain/zero/1.0/>.
 */

package dev.nikomaru.minecraftpluginmanager.repository.downloader.github

import dev.nikomaru.minecraftpluginmanager.MinecraftPluginManager
import dev.nikomaru.minecraftpluginmanager.data.DownloadData
import dev.nikomaru.minecraftpluginmanager.data.ManageData
import dev.nikomaru.minecraftpluginmanager.data.VersionData
import dev.nikomaru.minecraftpluginmanager.repository.downloader.UrlData
import dev.nikomaru.minecraftpluginmanager.repository.downloader.utils.DownloaderUtils
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


class GithubDownloader: KoinComponent {
    private val plugin: MinecraftPluginManager by inject()
    suspend fun download(data: UrlData.GithubUrlData, number: Int?) {
        plugin.logger.info("Downloading from github...")
        val release = getData(data)
        val name = data.repository
        val version = release.tagName.replace("v", "")

        plugin.logger.info("Latest version: $version")
        val assets = release.assets
        if (assets.isEmpty()) {
            plugin.logger.info("No assets found")
            return
        }
        val assetUrl: String = if (assets.size != 1) {
            if (number == null) {
                plugin.logger.info("複数のjarファイルが見つかりました。ダウンロードしたいアセット番号を指定してください。")
                assets.forEachIndexed { index, asset ->
                    plugin.logger.info("${index + 1}. ${asset.name}")
                }
                return
            }
            if (number > assets.size) {
                println("Invalid number")
                return
            }
            plugin.logger.info("Downloading ${assets[number].name}")
            assets[number].browserDownloadUrl
        } else {
            plugin.logger.info("Downloading ${assets[0].name}")
            assets[0].browserDownloadUrl
        }
        val file = plugin.dataFolder.parentFile.resolve("${name}-${version}.jar")
        val manageData = generateDownloadData(assetUrl, data, version, name)
        DownloaderUtils.download(assetUrl, file, manageData)
    }

    private fun generateDownloadData(
        assetUrl: String, data: UrlData.GithubUrlData, version: String, name: String
    ): ManageData {
        val downloadUrl = assetUrl.replace("https://github.com/${data.owner}/${data.repository}", "<repoId>")
            .replace(version, "<version.editedLatestVersion>")
        val versionData = VersionData(
            editedCurrentVersion = version, editedLatestVersion = version
        )
        val downloadData = DownloadData(
            autoUpdate = true, downloadUrl = downloadUrl
        )
        val manageData = ManageData(
            identify = name,
            version = versionData,
            repoId = "https://github.com/${data.owner}/${data.repository}",
            download = downloadData
        )
        return manageData
    }

    suspend fun getData(data: UrlData.GithubUrlData): GithubRelease = withContext(Dispatchers.IO) {
        val client = DownloaderUtils.client
        val url = "https://api.github.com/repos/${data.owner}/${data.repository}/releases/latest"
        val response = client.get(url).body<GithubRelease>()
        return@withContext response
    }

}