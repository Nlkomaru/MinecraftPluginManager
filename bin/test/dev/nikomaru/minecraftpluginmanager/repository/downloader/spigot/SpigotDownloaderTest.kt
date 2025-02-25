/*
 * Written in 2023-2024 by Nikomaru <nikomaru@nikomaru.dev>
 *
 * To the extent possible under law, the author(s) have dedicated all copyright and related and neighboring rights to this software to the public domain worldwide.This software is distributed without any warranty.
 *
 * You should have received a copy of the CC0 Public Domain Dedication along with this software.
 * If not, see <http://creativecommons.org/publicdomain/zero/1.0/>.
 */

package dev.nikomaru.minecraftpluginmanager.repository.downloader.spigot

import dev.nikomaru.minecraftpluginmanager.MinecraftPluginManagerTest
import dev.nikomaru.minecraftpluginmanager.repository.downloader.DownloadManager
import dev.nikomaru.minecraftpluginmanager.repository.downloader.UrlData
import dev.nikomaru.minecraftpluginmanager.repository.downloader.github.GithubDownloader
import kotlinx.coroutines.test.runTest
import org.koin.test.KoinTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MinecraftPluginManagerTest::class)
class SpigotDownloaderTest: KoinTest{
    private val urls = listOf(
        "https://www.spigotmc.org/resources/placeholderapi.6245/",
        "https://www.spigotmc.org/resources/vault.34315/"
    )
//    @Test
//    fun download() {
//        runTest {
//            val downloader = SpigotDownloader()
//            val manager = DownloadManager()
//            urls.forEach {
//                val urlData = manager.getURLData(it) as UrlData.SpigotmcUrlData
//                downloader.download(urlData, 0)
//            }
//        }
//    }

    @Test
    fun getLatestVersion() {
    }


}