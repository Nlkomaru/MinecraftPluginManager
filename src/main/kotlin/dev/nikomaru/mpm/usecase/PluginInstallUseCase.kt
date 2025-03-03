/*
 * Written in 2023-2024 by Nikomaru <nikomaru@nikomaru.dev>
 *
 * To the extent possible under law, the author(s) have dedicated all copyright and related and neighboring rights to this software to the public domain worldwide.This software is distributed without any warranty.
 *
 * You should have received a copy of the CC0 Public Domain Dedication along with this software.
 * If not, see <http://creativecommons.org/publicdomain/zero/1.0/>.
 */

package dev.nikomaru.mpm.usecase

import dev.nikomaru.mpm.domain.model.PluginData
import dev.nikomaru.mpm.domain.repository.DownloaderRepository
import dev.nikomaru.mpm.domain.repository.PluginRepository
import dev.nikomaru.mpm.utils.PluginDataUtils
import java.io.File
import org.bukkit.plugin.java.JavaPlugin
import org.koin.core.component.KoinComponent

/**
 * プラグインのインストールに関するユースケース
 * @property downloaderRepository ダウンローダーリポジトリ
 * @property pluginRepository プラグインリポジトリ
 * @property plugin Bukkitプラグインインスタンス
 */
class PluginInstallUseCase(
    private val downloaderRepository: DownloaderRepository,
    private val pluginRepository: PluginRepository,
    private val plugin: JavaPlugin
): KoinComponent {

    /**
     * リポジトリURLからプラグインをインストール
     * @param repositoryUrl リポジトリURL
     * @param number ダウンロードする数（複数ファイルがある場合）
     * @return インストールに成功した場合はtrue
     */
    suspend fun installPlugin(repositoryUrl: String, number: Int? = 1): Boolean {
        // リポジトリURLを正規化
        val normalizedUrl = if (repositoryUrl.endsWith("/")) {
            repositoryUrl.dropLast(1)
        } else {
            repositoryUrl
        }
        
        // プラグインをダウンロード
        val downloadedFile = downloaderRepository.downloadLatest(normalizedUrl, number) ?: return false

        val pluginData = PluginDataUtils.getPluginData(downloadedFile) ?: return false

        val installedPluginName = if(pluginData is PluginData.BukkitPluginData){
            pluginData.name
        }else{
            plugin.name
        }

        //すでに、同じ名前のプラグインがmanageされているか確認
        val installedPlugin = pluginRepository.getManagedPluginData(installedPluginName)
        if(installedPlugin != null){
            //すでにインストールされている
            return false
        }

        //プラグインをインストール
        
        
        return true
    }
    
    /**
     * ファイルからプラグインをインストール
     * @param file プラグインファイル
     * @return インストールに成功した場合はtrue
     */
    suspend fun installPluginFromFile(file: File): Boolean {
        // プラグインファイルからメタデータを抽出
        
        // プラグインをロード・有効化の処理を実装
        
        return true
    }

} 