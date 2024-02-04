/*
 * Written in 2023-2024 by Nikomaru <nikomaru@nikomaru.dev>
 *
 * To the extent possible under law, the author(s) have dedicated all copyright and related and neighboring rights to this software to the public domain worldwide.This software is distributed without any warranty.
 *
 * You should have received a copy of the CC0 Public Domain Dedication along with this software.
 * If not, see <http://creativecommons.org/publicdomain/zero/1.0/>.
 */

package dev.nikomaru.minecraftpluginmanager.commands

import dev.nikomaru.minecraftpluginmanager.utils.ComponentUtils.toComponent
import dev.nikomaru.minecraftpluginmanager.utils.ComponentUtils.toLegacyText
import revxrsal.commands.annotation.Command
import revxrsal.commands.annotation.Default
import revxrsal.commands.annotation.Description
import revxrsal.commands.annotation.Subcommand
import revxrsal.commands.bukkit.annotation.CommandPermission
import revxrsal.commands.command.CommandActor
import revxrsal.commands.help.CommandHelp

@Command("mpm")
@CommandPermission("mpm.command")
class HelpCommand {
    @Subcommand("help")
    @Description("Shows the help menu")
    fun help(sender: CommandActor, helpEntries: CommandHelp<String>, @Default("1") page: Int) {
        var message = ""
        for (entry in helpEntries.paginate(page, 7))  // 7 entries per page
            message += entry + "\n"
        sender.reply(
            message.toComponent().toLegacyText()
        )
    }
}