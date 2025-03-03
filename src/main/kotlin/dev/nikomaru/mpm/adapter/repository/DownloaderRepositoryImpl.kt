/*
 * Written in 2023-2024 by Nikomaru <nikomaru@nikomaru.dev>
 *
 * To the extent possible under law, the author(s) have dedicated all copyright and related and neighboring rights to this software to the public domain worldwide.This software is distributed without any warranty.
 *
 * You should have received a copy of the CC0 Public Domain Dedication along with this software.
 * If not, see <http://creativecommons.org/publicdomain/zero/1.0/>.
 */

package dev.nikomaru.mpm.adapter.repository

import dev.nikomaru.mpm.domain.model.repository.RepositoryType
import dev.nikomaru.mpm.domain.model.repository.UrlData
import dev.nikomaru.mpm.domain.model.repository.VersionData
import dev.nikomaru.mpm.domain.repository.DownloaderRepository
import dev.nikomaru.mpm.infrastructure.github.GithubDownloader
import dev.nikomaru.mpm.infrastructure.spigot.SpigotDownloader
import java.io.File
import org.bukkit.plugin.java.JavaPlugin

/**
 * DownloaderRepositoryの実装クラス
 * 各リポジトリタイプに応じたダウンロード処理を提供する
 */
class DownloaderRepositoryImpl(private val plugin: JavaPlugin) : DownloaderRepository {
    
    /**
     * URLからリポジトリタイプを判別
     * @param url 判別するURL
     * @return RepositoryType、該当なしの場合はnull
     */
    override fun getRepositoryType(url: String): RepositoryType? {
        return when {
            url.startsWith(RepositoryType.GITHUB.url) -> RepositoryType.GITHUB
            url.startsWith(RepositoryType.SPIGOTMC.url) -> RepositoryType.SPIGOTMC
            url.startsWith(RepositoryType.HANGER.url) -> RepositoryType.HANGER
            url.startsWith(RepositoryType.MODRINTH.url) -> RepositoryType.MODRINTH
            else -> null
        }
    }
    
    /**
     * URLからURLデータを抽出
     * @param url 抽出対象のURL
     * @return UrlDataオブジェクト、パースできない場合はnull
     */
    override fun getUrlData(url: String): UrlData? {
        val type = getRepositoryType(url) ?: return null
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
                UrlData.SpigotMcUrlData(resId)
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
    
    /**
     * 最新バージョンを取得
     * @param urlData URLデータ
     * @return 最新バージョン文字列
     */
    override suspend fun getLatestVersion(urlData: UrlData): VersionData {
        return when (urlData) {
            is UrlData.GithubUrlData -> {
                GithubDownloader().getLatestVersion(urlData)
            }
            is UrlData.SpigotMcUrlData -> {
                SpigotDownloader().getLatestVersion(urlData)
            }
            else -> {
                // 他のリポジトリタイプの実装
                VersionData("unknown","unknown")
            }
        }
    }
    
    /**
     * 指定バージョンのプラグインをダウンロード
     * @param urlData URLデータ
     * @param version バージョン
     * @param number ダウンロードする番号（複数ファイルがある場合）
     * @return ダウンロードしたファイル、失敗時はnull
     */
    override suspend fun downloadByVersion(urlData: UrlData, version: VersionData, number: Int?): File? {
        return when (urlData) {
            is UrlData.GithubUrlData -> {
                GithubDownloader().downloadByVersion(urlData, version, number)
            }
            is UrlData.SpigotMcUrlData -> {
                SpigotDownloader().downloadByVersion(urlData, version)
            }
            else -> {
                // 他のリポジトリタイプの実装
                null
            }
        }
    }
    
    /**
     * URLからプラグインをダウンロード
     * @param url ダウンロード元URL
     * @param number ダウンロードする番号（複数ファイルがある場合）
     * @return ダウンロードしたファイル、失敗時はnull
     */
    override suspend fun downloadLatest(url: String, number: Int?): File? {
        val type = getRepositoryType(url) ?: return null
        val urlData = getUrlData(url) ?: return null
        
        return when (type) {
            RepositoryType.GITHUB -> {
                val downloader = GithubDownloader()
                val latest = downloader.getLatestVersion(urlData as UrlData.GithubUrlData)
                downloader.downloadByVersion(urlData, latest, number)
            }
            
            RepositoryType.SPIGOTMC -> {
                // SpigotMCのダウンロード実装
                val downloader = SpigotDownloader()
                val latest = downloader.getLatestVersion(urlData as UrlData.SpigotMcUrlData)
                downloader.downloadByVersion(urlData, latest)
            }
            
            RepositoryType.HANGER -> {
                // Hangarのダウンロード実装
                null
            }
            
            RepositoryType.MODRINTH -> {
                // Modrinthのダウンロード実装
                null
            }
        }
    }
} 