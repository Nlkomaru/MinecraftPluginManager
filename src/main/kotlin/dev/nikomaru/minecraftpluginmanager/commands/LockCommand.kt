package dev.nikomaru.minecraftpluginmanager.commands

import revxrsal.commands.annotation.Command
import revxrsal.commands.annotation.Description
import revxrsal.commands.annotation.Subcommand
import revxrsal.commands.bukkit.annotation.CommandPermission
import revxrsal.commands.command.CommandActor

@Command("mpm")
@CommandPermission("mpm.command")
class LockCommand {
    @Subcommand("lock")
    @Description("プラグインをロックします。 ロックをしたプラグインは、`mpm outdatedAll`で更新されません。")
    fun lock(actor: CommandActor) {
    }

    @Subcommand("unlock")
    @Description("プラグインのロックを解除します。")
    fun unlock(actor: CommandActor) {
    }
}