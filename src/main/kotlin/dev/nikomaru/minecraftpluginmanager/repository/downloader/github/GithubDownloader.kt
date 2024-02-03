package dev.nikomaru.minecraftpluginmanager.repository.downloader.github

import dev.nikomaru.minecraftpluginmanager.MinecraftPluginManager
import dev.nikomaru.minecraftpluginmanager.repository.downloader.UrlData
import dev.nikomaru.minecraftpluginmanager.repository.downloader.utils.DownloaderUtils
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


class GithubDownloader : KoinComponent {
    val plugin: MinecraftPluginManager by inject()
    suspend fun download(data: UrlData.GithubUrlData, number: Int?) {
        println("Downloading from github...")
        val release = getData(data)
        val name = release.name
        val version = release.tagName.replace("v", "")
        println("Latest version: $version")
        val assets = release.assets
        if (assets.isEmpty()) {
            println("No assets found")
            return
        } else if (assets.size == 1) {
            println("Downloading ${assets[0].name}")
            val assetUrl = assets[0].browserDownloadUrl
            DownloaderUtils.download(assetUrl, plugin.dataFolder.parentFile.resolve("${name}-${version}.jar"))
        } else {
            if (number == null) {
                println("複数のjarファイルが見つかりました。ダウンロードしたいアセット番号を指定してください。")
                assets.forEachIndexed { index, asset ->
                    println("${index + 1}. ${asset.name}")
                }
                return
            }
            if (number > assets.size) {
                println("Invalid number")
                return
            }
            println("Downloading ${assets[number].name}")
            val assetUrl = assets[number].browserDownloadUrl
            DownloaderUtils.download(assetUrl, plugin.dataFolder.parentFile.resolve("${name}-${version}.jar"))
        }
    }

    suspend fun getData(data: UrlData.GithubUrlData): GithubRelease = withContext(Dispatchers.IO) {
        val client = DownloaderUtils.client
        val url = "https://api.github.com/repos/${data.owner}/${data.repository}/releases/latest"
        val response = client.get(url).body<GithubRelease>()
        return@withContext response
    }

}