/*
 * Written in 2023-2024 by Nikomaru <nikomaru@nikomaru.dev>
 *
 * To the extent possible under law, the author(s) have dedicated all copyright and related and neighboring rights to this software to the public domain worldwide.This software is distributed without any warranty.
 *
 * You should have received a copy of the CC0 Public Domain Dedication along with this software.
 * If not, see <http://creativecommons.org/publicdomain/zero/1.0/>.
 */

package dev.nikomaru.minecraftpluginmanager.commands.utils.parser

import dev.nikomaru.minecraftpluginmanager.MinecraftPluginManager
import dev.nikomaru.minecraftpluginmanager.value.PluginName
import org.bukkit.command.CommandSender
import org.incendo.cloud.context.CommandContext
import org.incendo.cloud.context.CommandInput
import org.incendo.cloud.parser.ArgumentParser
import org.incendo.cloud.parser.ParserDescriptor
import org.incendo.cloud.parser.ArgumentParseResult
import org.incendo.cloud.suggestion.BlockingSuggestionProvider
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PluginNameParser<CommandSender> : ArgumentParser<CommandSender, PluginName>, BlockingSuggestionProvider.Strings<CommandSender>, KoinComponent{
    val plugin : MinecraftPluginManager by inject()

    companion object{
        fun pluginNameParser(): ParserDescriptor<CommandSender, PluginName> {
            return ParserDescriptor.of(PluginNameParser(), PluginName::class.java)
        }
    }

    override fun stringSuggestions(commandContext: CommandContext<CommandSender>, input: CommandInput): MutableList<String> {
        return plugin.server.pluginManager.plugins.map { it.name }.toMutableList()
    }

    override fun parse(context: CommandContext<CommandSender & Any>, commandInput: CommandInput):
            ArgumentParseResult<PluginName> {
        val name = commandInput.readString()
        return ArgumentParseResult.success(PluginName(name))
    }
}