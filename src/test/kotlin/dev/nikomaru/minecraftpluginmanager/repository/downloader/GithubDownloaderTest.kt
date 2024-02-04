/*
 * Written in 2023-2024 by Nikomaru <nikomaru@nikomaru.dev>
 *
 * To the extent possible under law, the author(s) have dedicated all copyright and related and neighboring rights to this software to the public domain worldwide.This software is distributed without any warranty.
 *
 * You should have received a copy of the CC0 Public Domain Dedication along with this software.
 * If not, see <http://creativecommons.org/publicdomain/zero/1.0/>.
 */

package dev.nikomaru.minecraftpluginmanager.repository.downloader

import dev.nikomaru.minecraftpluginmanager.MinecraftPluginManagerTest
import dev.nikomaru.minecraftpluginmanager.repository.downloader.github.GithubDownloader
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.koin.test.KoinTest

@ExtendWith(MinecraftPluginManagerTest::class)
class GithubDownloaderTest : KoinTest {
    private val urls = listOf(
        "https://github.com/jpenilla/squaremap/",
        "https://github.com/EssentialsX/Essentials",
        "https://github.com/BlueMap-Minecraft/BlueMap",
        "https://github.com/AuthMe/AuthMeReloaded"
    )


    @Test
    fun download() {
        runTest {
            val downloader = GithubDownloader()
            val manager = DownloadManager()
            urls.forEach {
                val urlData = manager.getURLData(it) as UrlData.GithubUrlData
                downloader.download(urlData, 0)
            }
        }
    }

    @Test
    fun getData() {
        runTest {
            val downloader = GithubDownloader()
            val manager = DownloadManager()
            urls.forEach {
                val urlData = manager.getURLData(it) as UrlData.GithubUrlData
                val data = downloader.getData(urlData)
                println(data.tagName)
            }
        }
    }


}