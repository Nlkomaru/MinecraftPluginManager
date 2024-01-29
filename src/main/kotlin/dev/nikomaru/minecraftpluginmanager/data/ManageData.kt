package dev.nikomaru.minecraftpluginmanager.data

import kotlinx.serialization.Serializable

@Serializable
data class ManageData(
    val identify: String,
    val version: VersionData,
    val repoId : String,
    val download : DownloadData,
)
//  {
//    "identify": "MinecraftPluginManager",
//    "repoId": "https://github.com/Nlkomaru/MinecraftPluginManager/",
//    "version": VersionData,
//    "download": DownloadData
//  }

@Serializable
data class VersionData(
    val currentVersion: String,
    val editedCurrentVersionByRegex: String,
    val regex: String  = "^(0|[1-9]\\d*)\\.(0|[1-9]\\d*)\\.(0|[1-9]\\d*)(?:-((?:0|[1-9]\\d*|\\d*[a-zA-Z-][0-9a-zA-Z-]*)(?:\\.(?:0|[1-9]\\d*|\\d*[a-zA-Z-][0-9a-zA-Z-]*))*))?(?:\\+([0-9a-zA-Z-]+(?:\\.[0-9a-zA-Z-]+)*))?\$",
    val latestVersion: String,
    val editedLatestVersion: String
)
//  {
//    "currentVersion": "1.0.0",
//    "editedCurrentVersionByRegex": "1.0.0",
//    "regex": "^(0|[1-9]\\d*)\\.(0|[1-9]\\d*)\\.(0|[1-9]\\d*)(?:-((?:0|[1-9]\\d*|\\d*[a-zA-Z-][0-9a-zA-Z-]*)(?:\\.(?:0|[1-9]\\d*|\\d*[a-zA-Z-][0-9a-zA-Z-]*))*))?(?:\\+([0-9a-zA-Z-]+(?:\\.[0-9a-zA-Z-]+)*))?\$",
//    "latestVersion": "1.0.0",
//    "editedLatestVersion": "1.0.0"
//  }

// urlの後ろには、/をつけない システムで弾く
@Serializable
data class DownloadData(
    val url : String,
    val downloadUrl : String,
)
//  {
//    "url": "https://github.com/Nlkomaru/MinecraftPluginManager",
//    "downloadUrl": "<url>/releases/download/v<latestVersion>/MinecraftPluginManager_v<latestVersion>.jar"
//  }

