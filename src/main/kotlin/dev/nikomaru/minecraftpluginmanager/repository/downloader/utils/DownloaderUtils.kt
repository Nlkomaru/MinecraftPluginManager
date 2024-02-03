package dev.nikomaru.minecraftpluginmanager.repository.downloader.utils

import dev.nikomaru.minecraftpluginmanager.utils.Utils
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import org.codehaus.plexus.util.FileUtils
import java.io.File
import java.net.URL

object DownloaderUtils {
    val client = HttpClient(CIO) {
        expectSuccess = false
        install(ContentNegotiation) {
            json(json = Utils.json)
        }
    }

    fun download(url: String, file: File) {
        FileUtils.copyURLToFile(URL(url), file)
    }
}