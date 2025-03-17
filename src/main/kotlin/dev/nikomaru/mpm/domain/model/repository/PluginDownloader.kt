/*
 * Written in 2023-2025 by Nikomaru <nikomaru@nikomaru.dev>
 *
 * To the extent possible under law, the author(s) have dedicated all copyright and related and neighboring rights to this software to the public domain worldwide.This software is distributed without any warranty.
 *
 * You should have received a copy of the CC0 Public Domain Dedication along with this software.
 * If not, see <http://creativecommons.org/publicdomain/zero/1.0/>.
 */

package dev.nikomaru.mpm.domain.model.repository

import java.io.File

/**
 * プラグインダウンローダーのインターフェース
 * 各リポジトリ（SpigotMC、GitHub等）からのプラグインのダウンロードを担当する
 */
interface PluginDownloader {
    /**
     * リポジトリタイプの判定
     * @param url リポジトリURL
     * @return リポジトリタイプ、該当なしの場合はnull
     */
    fun getRepositoryType(url: String): RepositoryType?

    /**
     * URLデータの抽出
     * @param url リポジトリURL
     * @return 抽出したURLデータ
     */
    fun getUrlData(url: String): UrlData?

    /**
     * 最新バージョンの取得
     * @param urlData URLデータ
     * @return 最新バージョン
     */
    suspend fun getLatestVersion(urlData: UrlData): VersionData

    /**
     * 指定バージョンのプラグインをダウンロード
     * @param urlData URLデータ
     * @param version バージョン
     * @param number アセット番号（オプション）
     * @return ダウンロードしたファイル
     */
    suspend fun downloadByVersion(urlData: UrlData, version: VersionData, number: Int? = null): File?

    /**
     * リポジトリURLからプラグインをダウンロード
     * @param url リポジトリURL
     * @param number アセット番号（オプション）
     * @return ダウンロードしたファイル
     */
    suspend fun downloadLatest(url: String, number: Int? = null): File?
} 