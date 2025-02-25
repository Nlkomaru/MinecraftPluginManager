/*
 * Written in 2023-2024 by Nikomaru <nikomaru@nikomaru.dev>
 *
 * To the extent possible under law, the author(s) have dedicated all copyright and related and neighboring rights to this software to the public domain worldwide.This software is distributed without any warranty.
 *
 * You should have received a copy of the CC0 Public Domain Dedication along with this software.
 * If not, see <http://creativecommons.org/publicdomain/zero/1.0/>.
 */

package dev.nikomaru.minecraftpluginmanager.repository.downloader.spigot

import kotlinx.serialization.Serializable

@Serializable
data class SpigotBodyData(
    val downloads: Int,
    val name: String,
    val rating: Rating,
    val releaseDate: Long,
    val resource: Int,
    val uuid: String,
    val id: Int
)

@Serializable
data class Rating(
    val count: Int, val average: Double
)

@Serializable
data class SpigotDataDetails(
    val name: String, val tag: String, val file: SpigotDataDetailsFile
)

@Serializable
data class SpigotDataDetailsFile(
    val url: String,
)