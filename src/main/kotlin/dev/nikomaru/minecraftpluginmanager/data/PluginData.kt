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
    ) : PluginData()

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
    ) : PluginData()
}
