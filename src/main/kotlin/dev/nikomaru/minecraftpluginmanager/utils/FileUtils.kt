package dev.nikomaru.minecraftpluginmanager.utils

import kotlinx.serialization.json.Json

object FileUtils {
    val json = Json {
        prettyPrint = true
        encodeDefaults = true
        ignoreUnknownKeys = true
        isLenient = true
    }
}