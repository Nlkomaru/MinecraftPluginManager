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
data class ManageData(
    val identify: String,
    val version: VersionData,
    val repoId: String,
    val download: DownloadData,
) //  {

//    "identify": "MinecraftPluginManager",
//    "repoId": "https://github.com/Nlkomaru/MinecraftPluginManager/",
//    "version": VersionData,
//    "download": DownloadData
//  }
@Serializable
data class VersionData(
    val editedCurrentVersion: String, val editedLatestVersion: String
) //  {

//    "editedCurrentVersion": "1.0.0",
//    "editedLatestVersion": "1.0.0"
//  }
// urlの後ろには、/をつけない システムで弾く
@Serializable
data class DownloadData(
    val autoUpdate: Boolean,
    val downloadUrl: String,
) //  { "autoUpdate": true,
//      "downloadUrl": "<url>/releases/download/v<latestVersion>/MinecraftPluginManager_v<latestVersion>.jar"

//TODO: 内部について気にしなくていいようにする pluginごとにsealed classを作成することで処理、