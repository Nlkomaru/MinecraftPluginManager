package dev.nikomaru.minecraftpluginmanager.utils

import kotlinx.serialization.json.Json

object Utils {
    val json = Json {
        prettyPrint = true
        encodeDefaults = true
        ignoreUnknownKeys = true
        isLenient = true
    }

    fun Any.print() = println(this)
}