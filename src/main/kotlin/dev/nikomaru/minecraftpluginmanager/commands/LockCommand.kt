package dev.nikomaru.minecraftpluginmanager.commands

import revxrsal.commands.annotation.Command
import revxrsal.commands.annotation.Subcommand
import revxrsal.commands.bukkit.annotation.CommandPermission

@Command("mpm")
@CommandPermission("mpm.command")
class LockCommand {
    @Subcommand("lock")
    fun lock() {
    }

    @Subcommand("unlock")
    fun unlock() {
    }
}