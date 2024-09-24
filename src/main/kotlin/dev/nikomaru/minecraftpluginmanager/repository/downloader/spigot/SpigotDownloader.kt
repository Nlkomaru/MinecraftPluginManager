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
import dev.nikomaru.minecraftpluginmanager.data.VersionData
import dev.nikomaru.minecraftpluginmanager.repository.downloader.UrlData
import dev.nikomaru.minecraftpluginmanager.repository.downloader.abstract.AbstractDownloader
import dev.nikomaru.minecraftpluginmanager.repository.downloader.utils.DownloaderUtils
import io.ktor.client.call.*
import io.ktor.client.request.*
import org.koin.core.component.KoinComponent
import java.io.File

class SpigotDownloader: AbstractDownloader(), KoinComponent {
    override suspend fun download(
        data: UrlData, number: Int?
    ) { //https://api.spiget.org/v2/resources/${id}/versions/${getLatestVersion}/download
        data as UrlData.SpigotmcUrlData
        val url = "https://api.spiget.org/v2/resources/${data.resId}/versions/${getLatestVersion(data)}/download"
        val details = getDetails(data)
        val file = File("${getDetails(data).name}-${getLatestVersion(data)}.jar")
        val versionData = VersionData(getLatestVersion(data), getLatestVersion(data))
        val repoId = "https://www.spigotmc.org/${details.file.url.split("/").subList(0, 2).joinToString("/")}"
        val downloadData = DownloadData(
            true,
            "https://api.spiget.org/v2/resources/${data.resId}/versions/<version.editedLatestVersion>/download"
        )
        DownloaderUtils.download(url, file, ManageData(details.name, versionData, repoId, downloadData))
    }

    private suspend fun getDetails(data: UrlData): SpigotDataDetails {
        data as UrlData.SpigotmcUrlData
        val client = DownloaderUtils.client
        val url = "https://api.spiget.org/v2/resources/${data.resId}"
        return client.get(url).body<SpigotDataDetails>()

    }

    override suspend fun getLatestVersion(data: UrlData): String {
        data as UrlData.SpigotmcUrlData
        val client = DownloaderUtils.client
        val url = "https://api.spiget.org/v2/resources/${data.resId}/versions/latest"
        val response = client.get(url).body<SpigotBodyData>()
        return response.id.toString()
    }
}