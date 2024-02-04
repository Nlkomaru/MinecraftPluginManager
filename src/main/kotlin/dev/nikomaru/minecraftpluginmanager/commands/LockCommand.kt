/*
 * Written in 2023-2024 by Nikomaru <nikomaru@nikomaru.dev>
 *
 * To the extent possible under law, the author(s) have dedicated all copyright and related and neighboring rights to this software to the public domain worldwide.This software is distributed without any warranty.
 *
 * You should have received a copy of the CC0 Public Domain Dedication along with this software.
 * If not, see <http://creativecommons.org/publicdomain/zero/1.0/>.
 */

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