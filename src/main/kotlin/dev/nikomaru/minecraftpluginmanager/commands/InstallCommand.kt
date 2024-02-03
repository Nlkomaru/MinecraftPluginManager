package dev.nikomaru.minecraftpluginmanager.commands

import dev.nikomaru.minecraftpluginmanager.repository.downloader.DownloadManager
import org.bukkit.command.CommandSender
import revxrsal.commands.annotation.Command
import revxrsal.commands.annotation.Optional
import revxrsal.commands.bukkit.annotation.CommandPermission

@Command("mpm")
@CommandPermission("mpm.command")
class InstallCommand {

    @Command("install")
    suspend fun install(sender: CommandSender, repositoryUrl: String, @Optional number: Int?) {
        DownloadManager().download(repositoryUrl, number)
    }


}