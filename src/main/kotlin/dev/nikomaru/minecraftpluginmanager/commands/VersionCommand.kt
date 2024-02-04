/*
 * Written in 2023-2024 by Nikomaru <nikomaru@nikomaru.dev>
 *
 * To the extent possible under law, the author(s) have dedicated all copyright and related and neighboring rights to this software to the public domain worldwide.This software is distributed without any warranty.
 *
 * You should have received a copy of the CC0 Public Domain Dedication along with this software.
 * If not, see <http://creativecommons.org/publicdomain/zero/1.0/>.
 */

package dev.nikomaru.minecraftpluginmanager.commands

import dev.nikomaru.minecraftpluginmanager.MinecraftPluginManager
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import revxrsal.commands.annotation.Command
import revxrsal.commands.annotation.Description
import revxrsal.commands.annotation.Subcommand
import revxrsal.commands.bukkit.annotation.CommandPermission
import revxrsal.commands.command.CommandActor

@Command("mpm")
@CommandPermission("mpm.command")
class VersionCommand: KoinComponent {
    private val plugin: MinecraftPluginManager by inject()

    @Subcommand("version")
    @Description("Show version")
    fun version(actor: CommandActor) {
        actor.reply("MinecraftPluginManager version: ${plugin.pluginMeta.version}")
    }
}