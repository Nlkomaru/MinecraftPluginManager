package dev.nikomaru.minecraftpluginmanager.repository.downloader

sealed class UrlData {
    data class GithubUrlData(
        val owner: String,
        val repository: String
    ) : UrlData()

    data class SpigotmcUrlData(
        val id: String
    ) : UrlData()

    data class HangarUrlData(
        val id: String
    ) : UrlData()

    data class ModrinthUrlData(
        val id: String
    ) : UrlData()

}