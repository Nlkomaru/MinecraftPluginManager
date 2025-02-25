/*
 * Written in 2023-2024 by Nikomaru <nikomaru@nikomaru.dev>
 *
 * To the extent possible under law, the author(s) have dedicated all copyright and related and neighboring rights to this software to the public domain worldwide.This software is distributed without any warranty.
 *
 * You should have received a copy of the CC0 Public Domain Dedication along with this software.
 * If not, see <http://creativecommons.org/publicdomain/zero/1.0/>.
 */

package dev.nikomaru.minecraftpluginmanager.commands

import org.bukkit.command.CommandSender
import org.incendo.cloud.annotations.Command
import org.incendo.cloud.annotations.CommandDescription
import org.incendo.cloud.annotations.Permission


@Command("mpm")
@Permission("mpm.command")
class LockCommand {
    @Command("lock")
    @CommandDescription("プラグインをロックします。 ロックをしたプラグインは、`mpm outdatedAll`で更新されません。")
    fun lock(actor: CommandSender) {
    }

    @Command("unlock")
    @CommandDescription("プラグインのロックを解除します。")
    fun unlock(actor: CommandSender) {
    }
}