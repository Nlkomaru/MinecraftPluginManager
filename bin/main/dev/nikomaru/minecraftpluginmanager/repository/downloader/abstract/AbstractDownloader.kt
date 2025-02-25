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
    abstract suspend fun download(data: UrlData, number: Int?)

    abstract suspend fun getLatestVersion(data: UrlData): String

}