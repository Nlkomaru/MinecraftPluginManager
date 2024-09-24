/*
 * Written in 2023-2024 by Nikomaru <nikomaru@nikomaru.dev>
 *
 * To the extent possible under law, the author(s) have dedicated all copyright and related and neighboring rights to this software to the public domain worldwide.This software is distributed without any warranty.
 *
 * You should have received a copy of the CC0 Public Domain Dedication along with this software.
 * If not, see <http://creativecommons.org/publicdomain/zero/1.0/>.
 */

package dev.nikomaru.minecraftpluginmanager.repository.downloader

import dev.nikomaru.minecraftpluginmanager.repository.downloader.github.GithubDownloader

class DownloadManager {
    fun getType(url: String): RepositoryType? {
        return when {
            url.startsWith(RepositoryType.GITHUB.url) -> RepositoryType.GITHUB
            url.startsWith(RepositoryType.SPIGOTMC.url) -> RepositoryType.SPIGOTMC
            url.startsWith(RepositoryType.HANGER.url) -> RepositoryType.HANGER
            url.startsWith(RepositoryType.MODRINTH.url) -> RepositoryType.MODRINTH
            else -> null
        }
    }

    fun getURLData(url: String): UrlData {
        val type = getType(url)!!
        val formattedUrl = if (url.endsWith("/")) url.dropLast(1) else url
        return when (type) {
            RepositoryType.GITHUB -> {
                val split = formattedUrl.split("/")
                val owner = split[3]
                val repository = split[4]
                UrlData.GithubUrlData(owner, repository)
            }

            RepositoryType.SPIGOTMC -> {
                val resId = formattedUrl.split(".")[(url.split(".").size - 1)]
                UrlData.SpigotmcUrlData(resId)
            }

            RepositoryType.HANGER -> {
                val split = formattedUrl.split("/")
                val owner = split[3]
                val projectName = split[4]
                UrlData.HangarUrlData(owner, projectName)
            }

            RepositoryType.MODRINTH -> {
                val split = formattedUrl.split("/")
                val id = split[4]
                UrlData.ModrinthUrlData(id)
            }
        }
    }

    suspend fun download(url: String, number: Int?) {
        val type = getType(url) ?: return
        when (type) {
            RepositoryType.GITHUB -> {
                GithubDownloader().download(
                    getURLData(url) as UrlData.GithubUrlData, number
                )
            }

            RepositoryType.SPIGOTMC -> {
                getURLData(url) as UrlData.SpigotmcUrlData
            }

            RepositoryType.HANGER -> {
                println("Not implemented")
            }

            RepositoryType.MODRINTH -> {
                println("Not implemented")
            } //            -> SpigotmcDownloader().download(getURLData(url) as UrlData.SpigotmcUrlData)
            //            RepositoryType.HANGER -> HangarDownloader().download(getURLData(url) as UrlData.HangarUrlData)
            //            RepositoryType.MODRINTH -> ModrinthDownloader().download(getURLData(url) as UrlData.ModrinthUrlData)
        }
    }
}