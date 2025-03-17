/*
 * Written in 2023-2025 by Nikomaru <nikomaru@nikomaru.dev>
 *
 * To the extent possible under law, the author(s) have dedicated all copyright and related and neighboring rights to this software to the public domain worldwide.This software is distributed without any warranty.
 *
 * You should have received a copy of the CC0 Public Domain Dedication along with this software.
 * If not, see <http://creativecommons.org/publicdomain/zero/1.0/>.
 */

package dev.nikomaru.mpm

import dev.nikomaru.mpm.adapter.controller.InstallCommand
import dev.nikomaru.mpm.adapter.controller.ListCommand
import dev.nikomaru.mpm.adapter.repository.DownloaderRepositoryImpl
import dev.nikomaru.mpm.adapter.repository.PluginRepositoryImpl
import dev.nikomaru.mpm.domain.repository.DownloaderRepository
import dev.nikomaru.mpm.domain.repository.PluginRepository
import dev.nikomaru.mpm.usecase.PluginInstallUseCase
import dev.nikomaru.mpm.usecase.PluginListUseCase
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin
import org.incendo.cloud.annotations.AnnotationParser
import org.incendo.cloud.execution.ExecutionCoordinator
import org.incendo.cloud.kotlin.coroutines.annotations.installCoroutineSupport
import org.incendo.cloud.paper.LegacyPaperCommandManager
import org.incendo.cloud.setting.ManagerSetting
import org.koin.core.context.GlobalContext
import org.koin.dsl.module

/**
 * MinecraftPluginManagerのメインクラス
 * プラグインの起動・終了処理やDIコンテナの設定を担当
 */
class MinecraftPluginManager : JavaPlugin() {

    /**
     * プラグイン有効化時の処理
     * コマンドの設定とDIコンテナの初期化を行う
     */
    override fun onEnable() {
        // コマンドの設定
        setupCommands()
        
        // DIコンテナの初期化
        setupKoin()
        
        logger.info("MinecraftPluginManager has been enabled!")
    }

    /**
     * プラグイン無効化時の処理
     */
    override fun onDisable() {
        logger.info("MinecraftPluginManager has been disabled!")
    }

    /**
     * Koinのセットアップ
     * 依存性注入の設定を行う
     */
    private fun setupKoin() {
        // モジュールの定義
        val appModule = module {
            // プラグインインスタンス
            single<MinecraftPluginManager> { this@MinecraftPluginManager }
            single<JavaPlugin> { this@MinecraftPluginManager }
            
            // リポジトリの登録
            single<DownloaderRepository> { DownloaderRepositoryImpl(get()) }
            single<PluginRepository> { PluginRepositoryImpl(get()) }
            
            // ユースケースの登録
            single { PluginInstallUseCase(get(), get(), get()) }
            single { PluginListUseCase(get(), get()) }
        }

        // Koinの開始（すでに開始されている場合は何もしない）
        GlobalContext.getOrNull() ?: GlobalContext.startKoin {
            modules(appModule)
        }
    }

    /**
     * コマンドのセットアップ
     */
    private fun setupCommands() {
        // コマンドマネージャーの作成
        val commandManager = LegacyPaperCommandManager.createNative(
            this,
            ExecutionCoordinator.simpleCoordinator()
        )

        // 設定の調整
        commandManager.settings().set(ManagerSetting.ALLOW_UNSAFE_REGISTRATION, true)

        // コマンドパーサーの設定
        // commandManager.parserRegistry().registerParser(FileParser.fileParser())

        // アノテーションパーサーの作成と使用
        val annotationParser = AnnotationParser(commandManager, CommandSender::class.java)

        annotationParser.installCoroutineSupport()

        // コマンドの登録
        with(annotationParser) {
            parse(
                InstallCommand(),
                ListCommand(),
            )
        }
    }
}