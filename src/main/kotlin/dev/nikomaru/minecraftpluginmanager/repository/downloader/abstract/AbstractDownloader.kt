/*
 * Written in 2023-2024 by Nikomaru <nikomaru@nikomaru.dev>
 *
 * To the extent possible under law, the author(s) have dedicated all copyright and related and neighboring rights to this software to the public domain worldwide.This software is distributed without any warranty.
 *
 * You should have received a copy of the CC0 Public Domain Dedication along with this software.
 * If not, see <http://creativecommons.org/publicdomain/zero/1.0/>.
 */

package dev.nikomaru.minecraftpluginmanager.repository.downloader.abstract

import dev.nikomaru.minecraftpluginmanager.repository.downloader.UrlData

abstract class AbstractDownloader {

    /**
     * Download the latest version of the plugin.
     * @param data The data of the plugin.
     * @param number The number of the asset to download.
     */
    suspend fun download(data: UrlData, number: Int?){
        downloadByVersion(data, getLatestVersion(data), number)
    }

    /**
     * Get the versions of the plugin.
     * @param data The data of the plugin.
     * @return The list of versions of the plugin (ordered by the latest version).
     */
    abstract suspend fun getVersions(data: UrlData): List<String>

    /**
     * Get the latest version of the plugin.
     * @param data The data of the plugin.
     * @return The latest version of the plugin.
     */
    suspend fun getLatestVersion(data: UrlData): String {
        return getVersions(data).first()
    }

    /**
     * Download the plugin by the version.
     * @param data The data of the plugin.
     * @param version The version of the plugin.
     * @param number The number of the asset to download.
     */
    abstract suspend fun downloadByVersion(data: UrlData, version: String, number: Int?)
}