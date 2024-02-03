package dev.nikomaru.minecraftpluginmanager.commands

import dev.nikomaru.minecraftpluginmanager.MinecraftPluginManager
import org.bukkit.command.CommandSender
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import revxrsal.commands.annotation.Command
import revxrsal.commands.annotation.Description
import revxrsal.commands.annotation.Subcommand
import revxrsal.commands.bukkit.annotation.CommandPermission

@Command("mpm")
@CommandPermission("mpm.command")
class VersionCommand : KoinComponent {
    private val plugin: MinecraftPluginManager by inject()

    @Subcommand("version")
    @Description("Show version")
    fun version(actor: CommandSender) {
        actor.sendRichMessage(plugin.pluginMeta.version)
    }
}