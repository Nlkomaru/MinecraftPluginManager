/*
 * Written in 2023-2024 by Nikomaru <nikomaru@nikomaru.dev>
 *
 * To the extent possible under law, the author(s) have dedicated all copyright and related and neighboring rights to this software to the public domain worldwide.This software is distributed without any warranty.
 *
 * You should have received a copy of the CC0 Public Domain Dedication along with this software.
 * If not, see <http://creativecommons.org/publicdomain/zero/1.0/>.
 */

package dev.nikomaru.minecraftpluginmanager.repository.downloader

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.koin.test.KoinTest

class DownloadManagerTest: KoinTest {
    private val githubExampleUrl = "https://github.com/Nlkomaru/MinecraftPluginManager"
    private val spigotmcExampleUrl = "https://www.spigotmc.org/resources/plan-player-analytics.32536/"
    private val hangarExampleUrl = "https://hangar.papermc.io/AuroraLS3/Plan-Player-Analytics"
    private val modrinthExampleUrl = "https://modrinth.com/plugin/plasmo-voice"

    @Test
    fun getType() {
        val downloadManager = DownloadManager()
        assertEquals(RepositoryType.GITHUB, downloadManager.getType(githubExampleUrl))
        assertEquals(RepositoryType.SPIGOTMC, downloadManager.getType(spigotmcExampleUrl))
        assertEquals(RepositoryType.HANGER, downloadManager.getType(hangarExampleUrl))
        assertEquals(RepositoryType.MODRINTH, downloadManager.getType(modrinthExampleUrl))
    }

    @Test
    fun getURLData() {
        val downloadManager = DownloadManager()
        val githubUrlData = UrlData.GithubUrlData("Nlkomaru", "MinecraftPluginManager")
        assertEquals(githubUrlData, downloadManager.getURLData(githubExampleUrl))
        val spigotmcUrlData = UrlData.SpigotmcUrlData("32536")
        assertEquals(spigotmcUrlData, downloadManager.getURLData(spigotmcExampleUrl))
        val hangarUrlData = UrlData.HangarUrlData("AuroraLS3","Plan-Player-Analytics")
        assertEquals(hangarUrlData, downloadManager.getURLData(hangarExampleUrl))
        val modrinthUrlData = UrlData.ModrinthUrlData("plasmo-voice")
        assertEquals(modrinthUrlData, downloadManager.getURLData(modrinthExampleUrl))

    }

}