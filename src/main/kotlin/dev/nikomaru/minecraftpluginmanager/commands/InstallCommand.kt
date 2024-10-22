/*
 * Written in 2023-2024 by Nikomaru <nikomaru@nikomaru.dev>
 *
 * To the extent possible under law, the author(s) have dedicated all copyright and related and neighboring rights to this software to the public domain worldwide.This software is distributed without any warranty.
 *
 * You should have received a copy of the CC0 Public Domain Dedication along with this software.
 * If not, see <http://creativecommons.org/publicdomain/zero/1.0/>.
 */

package dev.nikomaru.minecraftpluginmanager.commands

import dev.nikomaru.minecraftpluginmanager.repository.downloader.DownloadManager
import org.bukkit.command.ConsoleCommandSender
import org.incendo.cloud.annotations.Argument
import org.incendo.cloud.annotations.Command
import org.incendo.cloud.annotations.Default
import org.incendo.cloud.annotations.Permission
import org.koin.core.component.KoinComponent


@Command("mpm")
@Permission("mpm.command")
class InstallCommand: KoinComponent {
    @Command("install <repositoryUrl> [number]")
    suspend fun install(
        sender: ConsoleCommandSender,
        @Argument("repositoryUrl") repositoryUrl: String,
        @Argument("number") @Default("1") number: Int?
    ) {
        if (repositoryUrl.isEmpty()) {
            sender.sendMessage("リポジトリのURLを入力してください")
            return
        }
        if (repositoryUrl.last() == '/') {
            repositoryUrl.dropLast(1)
        } else {
            repositoryUrl
        }.let { DownloadManager().download(it, number) }
    }

}