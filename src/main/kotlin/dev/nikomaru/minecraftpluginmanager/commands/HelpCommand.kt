package dev.nikomaru.minecraftpluginmanager.commands

import org.bukkit.command.CommandSender
import revxrsal.commands.annotation.Command
import revxrsal.commands.annotation.Default
import revxrsal.commands.annotation.Description
import revxrsal.commands.annotation.Subcommand
import revxrsal.commands.bukkit.annotation.CommandPermission
import revxrsal.commands.help.CommandHelp

@Command("mpm")
@CommandPermission("mpm.command")
class HelpCommand {
    @Subcommand("help")
    @Description("Shows the help menu")
    fun help(sender: CommandSender, helpEntries: CommandHelp<String>, @Default("1") page: Int) {
        var message = ""
        for (entry in helpEntries.paginate(page, 7))  // 7 entries per page
            message += entry + "\n"
        sender.sendMessage(message)
    }
}