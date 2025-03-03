/*
 * Written in 2023-2024 by Nikomaru <nikomaru@nikomaru.dev>
 *
 * To the extent possible under law, the author(s) have dedicated all copyright and related and neighboring rights to this software to the public domain worldwide.This software is distributed without any warranty.
 *
 * You should have received a copy of the CC0 Public Domain Dedication along with this software.
 * If not, see <http://creativecommons.org/publicdomain/zero/1.0/>.
 */

package dev.nikomaru.mpm.infrastructure.github

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
import kotlinx.serialization.json.*
import java.io.File
import java.time.LocalDateTime

/**
 * GitHubからプラグインをダウンロードするクラス
 */
class GithubDownloader {

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
     * 最新バージョンを取得
     * @param urlData GitHubのURL情報
     * @return 最新バージョン名
     */
    suspend fun getLatestVersion(urlData: UrlData.GithubUrlData): VersionData {
        val url = "https://api.github.com/repos/${urlData.owner}/${urlData.repository}/releases/latest"

        return withContext(Dispatchers.IO) {
            try {
                val response = client.get(url) {
                    headers {
                        append(HttpHeaders.Accept, "application/vnd.github+json")
                        append(HttpHeaders.UserAgent, "MinecraftPluginManager")
                    }
                }

                if (response.status.isSuccess()) {
                    val responseJson = json.parseToJsonElement(response.bodyAsText()).jsonObject
                    val id = responseJson["id"]?.jsonPrimitive?.content ?: "unknown"
                    val versionName = responseJson["tag_name"]?.jsonPrimitive?.content ?: "unknown"
                    return@withContext VersionData(downloadId = id, version = versionName)
                } else {
                    throw Exception("GitHubからの応答が失敗しました: ${response.status}")
                }
            } catch (e: Exception) {
                throw Exception("最新バージョンの取得に失敗しました: ${e.message}")
            }
        }
    }

    /**
     * 指定バージョンのプラグインをダウンロード
     * @param urlData GitHubのURL情報
     * @param version バージョン名
     * @param number アセット番号（複数アセットがある場合）
     * @return ダウンロードしたファイル
     */
    suspend fun downloadByVersion(urlData: UrlData.GithubUrlData, version: VersionData, number: Int?): File? {
        val url = "https://api.github.com/repos/${urlData.owner}/${urlData.repository}/releases/tags/$version"

        return withContext(Dispatchers.IO) {
            try {
                // リリース情報を取得
                val response = client.get(url) {
                    headers {
                        append(HttpHeaders.Accept, "application/vnd.github+json")
                        append(HttpHeaders.UserAgent, "MinecraftPluginManager")
                    }
                }

                if (!response.status.isSuccess()) {
                    throw Exception("GitHubからの応答が失敗しました: ${response.status}")
                }

                // JSONをパース
                val responseJson = json.parseToJsonElement(response.bodyAsText()).jsonObject
                val assets = responseJson["assets"]?.jsonArray ?: return@withContext null

                // アセットの確認
                if (assets.isEmpty()) {
                    throw Exception("このリリースにはアセットがありません")
                }

                // 指定したアセット番号（または最初のアセット）を選択
                val assetIndex = (number ?: 1).coerceIn(1, assets.size) - 1
                val asset = assets[assetIndex].jsonObject

                // ダウンロードURL取得
                val downloadUrl = asset["browser_download_url"]?.jsonPrimitive?.content
                    ?: throw Exception("ダウンロードURLが見つかりません")

                // ファイル名取得
                val fileName = asset["name"]?.jsonPrimitive?.content ?: "plugin-${LocalDateTime.now()}.jar"

                // ファイルをダウンロード
                val fileResponse = client.get(downloadUrl)
                if (!fileResponse.status.isSuccess()) {
                    throw Exception("ファイルのダウンロードに失敗しました: ${fileResponse.status}")
                }

                // 一時ファイルに保存
                val tempFile = File.createTempFile("plugin-", "-${fileName}")
                tempFile.writeBytes(fileResponse.body())

                tempFile
            } catch (e: Exception) {
                println("プラグインのダウンロードに失敗しました: ${e.message}")
                null
            }
        }
    }
} 