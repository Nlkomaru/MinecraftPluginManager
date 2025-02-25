/*
 * Written in 2023-2024 by Nikomaru <nikomaru@nikomaru.dev>
 *
 * To the extent possible under law, the author(s) have dedicated all copyright and related and neighboring rights to this software to the public domain worldwide.This software is distributed without any warranty.
 *
 * You should have received a copy of the CC0 Public Domain Dedication along with this software.
 * If not, see <http://creativecommons.org/publicdomain/zero/1.0/>.
 */

package dev.nikomaru.minecraftpluginmanager.repository.downloader.spigot

import dev.nikomaru.minecraftpluginmanager.data.DownloadData
import dev.nikomaru.minecraftpluginmanager.data.ManageData
import dev.nikomaru.minecraftpluginmanager.data.RepositoryData
import dev.nikomaru.minecraftpluginmanager.data.VersionData
import dev.nikomaru.minecraftpluginmanager.repository.downloader.UrlData
import dev.nikomaru.minecraftpluginmanager.repository.downloader.abstract.AbstractDownloader
import dev.nikomaru.minecraftpluginmanager.repository.downloader.utils.DownloaderUtils
import io.ktor.client.call.*
import io.ktor.client.request.*
import org.koin.core.component.KoinComponent
import java.io.File

class SpigotDownloader: AbstractDownloader(), KoinComponent {

    private suspend fun getDetails(data: UrlData): SpigotDataDetails {
        data as UrlData.SpigotmcUrlData
        val client = DownloaderUtils.client
        val url = "https://api.spiget.org/v2/resources/${data.resId}"
        return client.get(url).body<SpigotDataDetails>()
    }

    override suspend fun getVersions(data: UrlData): List<String> {
        data as UrlData.SpigotmcUrlData
        val client = DownloaderUtils.client
        val url = "https://api.spiget.org/v2/resources/${data.resId}/versions"
        val response = client.get(url).body<List<SpigotBodyData>>()
        return response.map { it.id.toString() }
    }

    override suspend fun downloadByVersion(data: UrlData, version: String, number: Int?) {
        data as UrlData.SpigotmcUrlData
        val url = "https://api.spiget.org/v2/resources/${data.resId}/versions/${version}/download"
        val details = getDetails(data)
        val file = File("${getDetails(data).name}-${version}.jar")
        
        val repositoryData = RepositoryData.SpigotData(
            resId = data.resId
        )
        
        val versionData = VersionData(
            rawCurrentVersion = version,
            rawLatestVersion = getLatestVersion(data),
            editRegex = "(.*)",
            editedCurrentVersion = version,
            editedLatestVersion = getLatestVersion(data)
        )
        
        val downloadUrl = "https://api.spiget.org/v2/resources/${data.resId}/versions/<version.rawLatestVersion>/download"
        val downloadData = DownloadData(
            autoUpdate = true, 
            downloadUrl = downloadUrl
        )
        
        val manageData = ManageData(
            identify = details.name,
            repositories = repositoryData,
            version = versionData,
            download = downloadData
        )
        
        DownloaderUtils.download(url, file, manageData)
    }
}