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
import org.bukkit.command.CommandSender
import org.incendo.cloud.context.CommandContext
import org.incendo.cloud.context.CommandInput
import org.incendo.cloud.parser.ArgumentParseResult
import org.incendo.cloud.parser.ArgumentParser
import org.incendo.cloud.parser.ParserDescriptor
import org.incendo.cloud.suggestion.BlockingSuggestionProvider
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.File
import java.nio.file.FileSystemNotFoundException

class FileParser<CommandSender> : ArgumentParser<CommandSender, File>, BlockingSuggestionProvider.Strings<CommandSender>, KoinComponent{
    val plugin : MinecraftPluginManager by inject()

    companion object{
        fun fileParser(): ParserDescriptor<CommandSender, File> {
            return ParserDescriptor.of(FileParser(), File::class.java)
        }
    }

    override fun stringSuggestions(commandContext: CommandContext<CommandSender>, input: CommandInput): MutableList<String> {
        return plugin.dataFolder.parentFile.listFiles().filter{ it.name.endsWith(".jar") }.map { it.name }.toMutableList()
    }

    override fun parse(context: CommandContext<CommandSender & Any>, commandInput: CommandInput):
            ArgumentParseResult<File> {
        val file = plugin.dataFolder.parentFile.resolve(commandInput.readString())
        return if (file.exists()) {
            ArgumentParseResult.success(file)
        } else {
            ArgumentParseResult.failure(FileSystemNotFoundException("File not found"))
        }
    }
}