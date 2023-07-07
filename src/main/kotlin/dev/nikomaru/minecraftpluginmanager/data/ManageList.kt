package dev.nikomaru.minecraftpluginmanager.data

import kotlinx.serialization.Serializable

@Serializable
data class ManageList(val list :ArrayList<PluginData> = arrayListOf())

@Serializable
data class PluginData(
    val identify: String,
    val name: String,
    val currentVersion: String,
    val editedCurrentVersionPrefix : String = "v<cutCurrentVersion>",
    val editedCurrentVersion : String,
    val latestVersion: String,
    val editedLatestVersionPrefix : String = "v<cutGetLatestVersion>",
    val editedLatestVersion: String,
    val repositoryUrl : String,
    val downloadIdentify : String = "",
    val autoInfoUpdate : Boolean = false,
    val latestUrlPrefix : String = "<downloadIdentify>",
    val latestUrl : String,
    val install : Boolean = false
)

