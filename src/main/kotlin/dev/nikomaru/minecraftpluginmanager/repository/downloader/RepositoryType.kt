package dev.nikomaru.minecraftpluginmanager.repository.downloader

enum class RepositoryType(val url: String) {
    GITHUB("https://github.com"),
    SPIGOTMC("https://www.spigotmc.org/resources"),
    HANGER("https://hangar.papermc.io"),
    MODRINTH("https://modrinth.com/plugin"),
}