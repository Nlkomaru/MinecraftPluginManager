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
import dev.nikomaru.minecraftpluginmanager.data.RepositoryData
import dev.nikomaru.minecraftpluginmanager.data.VersionData
import dev.nikomaru.minecraftpluginmanager.repository.downloader.UrlData
import dev.nikomaru.minecraftpluginmanager.repository.downloader.abstract.AbstractDownloader
import dev.nikomaru.minecraftpluginmanager.repository.downloader.utils.DownloaderUtils
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


class GithubDownloader: KoinComponent, AbstractDownloader() {
    private val plugin: MinecraftPluginManager by inject()

    override suspend fun downloadByVersion(data: UrlData, version: String, number: Int?) {
        plugin.logger.info("Downloading from github...")
        data as UrlData.GithubUrlData
        val release = getData(data, version)
        val name = data.repository
        val tag = release.tagName.replace("v", "")
        plugin.logger.info("Latest version: $tag")
        val assets = release.assets
        if (assets.isEmpty()) {
            plugin.logger.info("アセットが見つかりません")
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
                println("指定された番号のアセットが見つかりません")
                return
            }
            plugin.logger.info("${assets[number].name} をダウンロードしています...")
            assets[number].browserDownloadUrl
        } else {
            plugin.logger.info("${assets[0].name} をダウンロードしています...")
            assets[0].browserDownloadUrl
        }
        val file = plugin.dataFolder.parentFile.resolve("${name}-${tag}.jar")
        val manageData = generateDownloadData(assetUrl, data, release.tagName, tag, name)
        DownloaderUtils.download(assetUrl, file, manageData)
    }

    override suspend fun getVersions(data: UrlData): List<String> = withContext(Dispatchers.IO) {
        val client = DownloaderUtils.client
        data as UrlData.GithubUrlData
        val url = "https://api.github.com/repos/${data.owner}/${data.repository}/releases"
        val response = client.get(url).body<List<GithubRelease>>()
        return@withContext response.map { it.tagName }
    }


    private fun generateDownloadData(
        assetUrl: String, data: UrlData.GithubUrlData, rawVersion: String, editedVersion: String, name: String
    ): ManageData {
        val downloadUrl = assetUrl.replace("https://github.com/${data.owner}/${data.repository}", "<repoId>")
            .replace(rawVersion, "<version.rawLatestVersion>")
        
        val repositoryData = RepositoryData.GithubData(
            owner = data.owner,
            repository = data.repository
        )
        
        val versionData = VersionData(
            rawCurrentVersion = rawVersion,
            rawLatestVersion = rawVersion,
            editRegex = "v(.*)",
            editedCurrentVersion = editedVersion,
            editedLatestVersion = editedVersion
        )
        
        val downloadData = DownloadData(
            autoUpdate = true, 
            downloadUrl = downloadUrl
        )
        
        return ManageData(
            identify = name,
            repositories = repositoryData,
            version = versionData,
            download = downloadData
        )
    }

    suspend fun getData(data: UrlData.GithubUrlData, version: String): GithubRelease = withContext(Dispatchers.IO) {
        val client = DownloaderUtils.client
        val url = "https://api.github.com/repos/${data.owner}/${data.repository}/releases/tags/${version}"
        val response = client.get(url).body<GithubRelease>()
        return@withContext response
    }

}