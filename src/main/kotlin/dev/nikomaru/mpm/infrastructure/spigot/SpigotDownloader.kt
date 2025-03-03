/*
 * Written in 2023-2024 by Nikomaru <nikomaru@nikomaru.dev>
 *
 * To the extent possible under law, the author(s) have dedicated all copyright and related and neighboring rights to this software to the public domain worldwide.This software is distributed without any warranty.
 *
 * You should have received a copy of the CC0 Public Domain Dedication along with this software.
 * If not, see <http://creativecommons.org/publicdomain/zero/1.0/>.
 */

package dev.nikomaru.mpm.infrastructure.spigot

import dev.nikomaru.mpm.domain.model.repository.RepositoryType
import dev.nikomaru.mpm.domain.model.repository.UrlData
import dev.nikomaru.mpm.domain.model.repository.VersionData
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.io.File

/**
 * SpigotMCからプラグインをダウンロードするクラス
 */
class SpigotDownloader {

    // HTTP クライアント
    private val client = HttpClient(CIO) {
        install(HttpTimeout) {
            requestTimeoutMillis = 60000
            connectTimeoutMillis = 60000
            socketTimeoutMillis = 60000
        }
    }

    // JSONパーサー
    private val json = Json { ignoreUnknownKeys = true }

    /**
     * リソース詳細情報を取得
     * @param urlData SpigotMCのURL情報
     * @return リソース詳細情報
     */
    private suspend fun getDetails(urlData: UrlData.SpigotMcUrlData): SpigotResourceDetails {
        val url = "https://api.spiget.org/v2/resources/${urlData.resourceId}"

        return withContext(Dispatchers.IO) {
            try {
                val response = client.get(url) {
                    headers {
                        append(HttpHeaders.Accept, "application/json")
                        append(HttpHeaders.UserAgent, "MinecraftPluginManager")
                    }
                }

                if (response.status.isSuccess()) {
                    json.decodeFromString<SpigotResourceDetails>(response.bodyAsText())
                } else {
                    throw Exception("SpigotMCからの応答が失敗しました: ${response.status}")
                }
            } catch (e: Exception) {
                throw Exception("リソース詳細の取得に失敗しました: ${e.message}")
            }
        }
    }

    /**
     * リポジトリタイプの判定
     * @param url リポジトリURL
     * @return リポジトリタイプ、該当なしの場合はnull
     */
    fun getRepositoryType(url: String): RepositoryType? {
        val spigotPattern = Regex("https?://(?:www\\.)?spigotmc\\.org/resources/(?:.+\\.)?([0-9]+)(?:/.*)?")
        return if (spigotPattern.matches(url)) RepositoryType.SPIGOTMC else null
    }

    /**
     * URLデータの抽出
     * @param url リポジトリURL
     * @return 抽出したURLデータ
     */
    fun getUrlData(url: String): UrlData? {
        val spigotPattern = Regex("https?://(?:www\\.)?spigotmc\\.org/resources/(?:.+\\.)?([0-9]+)(?:/.*)?")
        val match = spigotPattern.find(url) ?: return null
        val resourceId = match.groupValues[1]
        return UrlData.SpigotMcUrlData(resourceId)
    }

    /**
     * 最新バージョンの取得
     * @param urlData URLデータ
     * @return 最新バージョン
     */
    suspend fun getLatestVersion(urlData: UrlData): VersionData {
        urlData as UrlData.SpigotMcUrlData
        val url = "https://api.spiget.org/v2/resources/${urlData.resourceId}/versions?sort=-name&size=1"

        return withContext(Dispatchers.IO) {
            try {
                val response = client.get(url) {
                    headers {
                        append(HttpHeaders.Accept, "application/json")
                        append(HttpHeaders.UserAgent, "MinecraftPluginManager")
                    }
                }

                if (response.status.isSuccess()) {
                    val responseJson = json.parseToJsonElement(response.bodyAsText()).jsonArray
                    val version = responseJson[0].jsonObject["name"]?.jsonPrimitive?.content ?: "unknown"
                    val id = responseJson[0].jsonObject["id"]?.jsonPrimitive?.content ?: "unknown"
                    return@withContext VersionData(downloadId = id, version = version)
                } else {
                    throw Exception("SpigotMCからの応答が失敗しました: ${response.status}")
                }
            } catch (e: Exception) {
                throw Exception("最新バージョンの取得に失敗しました: ${e.message}")
            }
        }
    }

    /**
     * 指定バージョンのプラグインをダウンロード
     * @param urlData URLデータ
     * @param version バージョン
     * @return ダウンロードしたファイル
     */
    suspend fun downloadByVersion(urlData: UrlData, version: VersionData): File? {
        urlData as UrlData.SpigotMcUrlData
        val details = getDetails(urlData)
        val downloadUrl = "https://api.spiget.org/v2/resources/${urlData.resourceId}/versions/${version.downloadId}/download"

        return withContext(Dispatchers.IO) {
            try {
                // ファイルをダウンロード
                val fileResponse = client.get(downloadUrl) {
                    headers {
                        append(HttpHeaders.Accept, "application/java-archive")
                        append(HttpHeaders.UserAgent, "MinecraftPluginManager")
                    }
                }

                if (!fileResponse.status.isSuccess()) {
                    throw Exception("ファイルのダウンロードに失敗しました: ${fileResponse.status}")
                }

                // ファイル名の生成
                val fileName = "${details.name}-$version.jar"

                // 一時ファイルに保存
                val tempFile = File.createTempFile("plugin-", "-$fileName")
                tempFile.writeBytes(fileResponse.body())

                tempFile
            } catch (e: Exception) {
                println("プラグインのダウンロードに失敗しました: ${e.message}")
                null
            }
        }
    }

    /**
     * リポジトリURLからプラグインをダウンロード
     * @param url リポジトリURL
     * @param number 未使用パラメータ（SpigotMCでは使用しない）
     * @return ダウンロードしたファイル
     */
    suspend fun downloadLatest(url: String, number: Int?): File? {
        val urlData = getUrlData(url) ?: throw Exception("無効なSpigotMC URL: $url")
        val latestVersion = getLatestVersion(urlData)
        return downloadByVersion(urlData, latestVersion)
    }
}

/**
 * SpigotMCリソース詳細情報
 */
@Serializable
data class SpigotResourceDetails(
    val id: Int,
    val name: String,
    val tag: String? = null,
    val version: SpigotVersionInfo? = null
)

/**
 * SpigotMCバージョン情報
 */
@Serializable
data class SpigotVersionInfo(
    val id: Int,
    val name: String
)

/**
 * SpigotMCバージョンデータ
 */
@Serializable
data class SpigotVersionData(
    val id: Int,
    val name: String,
    val releaseDate: Long
)