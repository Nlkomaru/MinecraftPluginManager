/*
 * Written in 2023-2024 by Nikomaru <nikomaru@nikomaru.dev>
 *
 * To the extent possible under law, the author(s) have dedicated all copyright and related and neighboring rights to this software to the public domain worldwide.This software is distributed without any warranty.
 *
 * You should have received a copy of the CC0 Public Domain Dedication along with this software.
 * If not, see <http://creativecommons.org/publicdomain/zero/1.0/>.
 */

package dev.nikomaru.minecraftpluginmanager.data

import kotlinx.serialization.Serializable

@Serializable
sealed class PluginData {
    @Serializable
    data class BukkitPluginData(
        val name: String = "",
        val version: String = "",
        val main: String = "",
        val description: String = "",
        val author: String = "",
        val website: String = "",
        val apiVersion: String = "",
    ): PluginData()

    @Serializable
    data class PaperPluginData(
        val name: String = "",
        val version: String = "",
        val main: String = "",
        val description: String = "",
        val apiVersion: String = "",
        val bootstrapper: String = "",
        val loader: String = "",
        val author: String = "",
        val website: String = "",
    ): PluginData()

    fun getPluginName(): String {
        return when (this) {
            is BukkitPluginData -> {
                this.name
            }

            is PaperPluginData -> {
                this.name
            }
        }
    }
}
