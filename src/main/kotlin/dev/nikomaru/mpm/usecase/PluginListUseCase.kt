/*
 * Written in 2023-2025 by Nikomaru <nikomaru@nikomaru.dev>
 *
 * To the extent possible under law, the author(s) have dedicated all copyright and related and neighboring rights to this software to the public domain worldwide.This software is distributed without any warranty.
 *
 * You should have received a copy of the CC0 Public Domain Dedication along with this software.
 * If not, see <http://creativecommons.org/publicdomain/zero/1.0/>.
 */

package dev.nikomaru.mpm.usecase

import dev.nikomaru.mpm.domain.model.PluginData
import dev.nikomaru.mpm.domain.repository.PluginRepository
import org.bukkit.plugin.java.JavaPlugin

/**
 * プラグインリスト表示に関するユースケース
 * @property pluginRepository プラグインリポジトリ
 * @property plugin Bukkitプラグインインスタンス
 */
class PluginListUseCase(
    private val pluginRepository: PluginRepository,
    private val plugin: JavaPlugin
) {
    
    /**
     * 管理下のすべてのプラグインを取得
     * @return 管理下のプラグインのリスト
     */
    suspend fun getAllManagedPlugins(): List<PluginData> {
        return pluginRepository.getAllManagedPluginData()
    }
    
    /**
     * サーバー上の全プラグインの状態を取得
     * @return プラグイン名とその状態（有効/無効）のマップ
     */
    fun getAllServerPlugins(): Map<String, Boolean> {
        return plugin.server.pluginManager.plugins.associate { 
            it.name to it.isEnabled 
        }
    }
    
    /**
     * 管理下にないプラグインを取得
     * @return 管理下にないプラグイン名のリスト
     */
    suspend fun getUnmanagedPlugins(): List<String> {
        val managedPlugins = getAllManagedPlugins().map {
            when (it) {
                is PluginData.BukkitPluginData -> it.name
                is PluginData.PaperPluginData -> it.name
            }
        }.toSet()
        
        return getAllServerPlugins().keys.filter { 
            it !in managedPlugins && it != plugin.name 
        }
    }
    
    /**
     * 有効なプラグインのみを取得
     * @return 有効なプラグインのリスト
     */
    suspend fun getEnabledPlugins(): List<PluginData> {
        val enabledPluginNames = getAllServerPlugins()
            .filter { it.value }
            .map { it.key }
            .toSet()
        
        return getAllManagedPlugins().filter {
            val name = when (it) {
                is PluginData.BukkitPluginData -> it.name
                is PluginData.PaperPluginData -> it.name
            }
            name in enabledPluginNames
        }
    }
    
    /**
     * 無効なプラグインのみを取得
     * @return 無効なプラグインのリスト
     */
    suspend fun getDisabledPlugins(): List<PluginData> {
        val disabledPluginNames = getAllServerPlugins()
            .filter { !it.value }
            .map { it.key }
            .toSet()
        
        return getAllManagedPlugins().filter {
            val name = when (it) {
                is PluginData.BukkitPluginData -> it.name
                is PluginData.PaperPluginData -> it.name
            }
            name in disabledPluginNames
        }
    }
} 