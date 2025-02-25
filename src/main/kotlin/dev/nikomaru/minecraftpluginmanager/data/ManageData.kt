/*
 * Written in 2023-2024 by Nikomaru <nikomaru@nikomaru.dev>
 *
 * To the extent possible under law, the author(s) have dedicated all copyright and related and neighboring rights to this software to the public domain worldwide.This software is distributed without any warranty.
 *
 * You should have received a copy of the CC0 Public Domain Dedication along with this software.
 * If not, see <http://creativecommons.org/publicdomain/zero/1.0/>.
 */

package dev.nikomaru.minecraftpluginmanager.data

import kotlinx.serialization.Serializable

/**
 * プラグインの管理情報を表すデータクラス
 * @param identify プラグインの識別子
 * @param repositories リポジトリ情報
 * @param version バージョン情報
 * @param download ダウンロード設定
 */
@Serializable
data class ManageData(
    val identify: String,
    val repositories: RepositoryData,
    val version: VersionData,
    val download: DownloadData,
)

/**
 * リポジトリ情報を表す封印クラス
 */
@Serializable
sealed class RepositoryData {
    /**
     * Modrinthリポジトリの情報
     * @param id Modrinthプロジェクトのid
     */
    @Serializable
    data class ModrinthData(val id: String) : RepositoryData()

    /**
     * Spigotリポジトリの情報
     * @param resId Spigotリソースのid
     */
    @Serializable
    data class SpigotData(val resId: String) : RepositoryData()

    /**
     * Hangarリポジトリの情報
     * @param owner プロジェクトのオーナー名
     * @param projectName プロジェクト名
     */
    @Serializable
    data class HangarData(val owner: String, val projectName: String) : RepositoryData()

    /**
     * Githubリポジトリの情報
     * @param owner リポジトリのオーナー名
     * @param repository リポジトリ名
     */
    @Serializable
    data class GithubData(val owner: String, val repository: String) : RepositoryData()

    /**
     * Jenkinsリポジトリの情報
     * @param url JenkinsサーバーのベースURL
     * @param job ジョブ名
     * @param artifact アーティファクトのパス
     */
    @Serializable
    data class JenkinsData(val url: String, val job: String, val artifact: String) : RepositoryData()
}

/**
 * バージョン情報を表すデータクラス
 * @param rawCurrentVersion 生のバージョン文字列（例：v1.0.0）
 * @param rawLatestVersion 生の最新バージョン文字列（例：v1.0.0）
 * @param editRegex バージョン編集用の正規表現（例：v(.*)）
 * @param editedCurrentVersion 編集後の現在バージョン（例：1.0.0）
 * @param editedLatestVersion 編集後の最新バージョン（例：1.0.0）
 */
@Serializable
data class VersionData(
    val rawCurrentVersion: String = "",
    val rawLatestVersion: String = "",
    val editRegex: String = "",
    val editedCurrentVersion: String,
    val editedLatestVersion: String
)

/**
 * ダウンロード設定を表すデータクラス
 * @param autoUpdate 自動更新の有効/無効
 * @param downloadUrl ダウンロードURL（バージョン置換用の特殊構文を含む）
 */
@Serializable
data class DownloadData(
    val autoUpdate: Boolean,
    val downloadUrl: String,
)