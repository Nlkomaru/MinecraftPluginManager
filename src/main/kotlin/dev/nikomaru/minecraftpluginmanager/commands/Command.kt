package dev.nikomaru.minecraftpluginmanager.commands

import cloud.commandframework.annotations.CommandMethod
import cloud.commandframework.annotations.CommandPermission
import dev.nikomaru.minecraftpluginmanager.MinecraftPluginManager
import dev.nikomaru.minecraftpluginmanager.MinecraftPluginManager.Companion.plugin
import dev.nikomaru.minecraftpluginmanager.data.ManageList
import dev.nikomaru.minecraftpluginmanager.data.PluginData
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.apache.commons.io.FileUtils
import org.bukkit.Bukkit
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedOutputStream
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL


@CommandMethod("mpm")
@CommandPermission("mpm.command")
class Command {

    val jsonFormant = Json {
        prettyPrint = true
        encodeDefaults = true
    }

    @CommandMethod("init")
    fun create() {
        val list = ManageList()
        Bukkit.getPluginManager().plugins.forEach {
            val pluginMeta = it.pluginMeta
            val data = PluginData(
                identify = pluginMeta.name,
                name = pluginMeta.name.lowercase(),
                currentVersion = pluginMeta.version,
                editedCurrentVersion = "v${pluginMeta.version.replace("/[^0-9.]/".toRegex(), "")}",
                latestVersion = "",
                editedLatestVersion = "",
                repositoryUrl = pluginMeta.website ?: "",
                latestUrl = ""
            )
            list.list.add(data)
        }
        val file = plugin.dataFolder.resolve("manageList.json")
        plugin.dataFolder.mkdirs()
        file.createNewFile()
        file.writeText(jsonFormant.encodeToString(list))
        plugin.logger.info("manageList.json is created")
    }

    @CommandMethod("info")
    fun updateInfo() {
        val file = plugin.dataFolder.resolve("manageList.json")
        if (!file.exists()) {
            plugin.logger.warning("manageList.jsonが存在しません。")
            return
        }
        val list = jsonFormant.decodeFromString<ManageList>(file.readText())
        val newDataList = arrayListOf<PluginData>()
        list.list.forEach { data ->
            if (data.autoInfoUpdate) {
                val plugin = Bukkit.getPluginManager().getPlugin(data.identify)
                if (plugin == null) {
                    MinecraftPluginManager.plugin.logger.warning("${data.identify} is not found")
                } else {
                    val latestVersion = getLatestVersion(data.repositoryUrl)
                    val newData = PluginData(
                        identify = data.identify,
                        name = plugin.pluginMeta.name,
                        currentVersion = plugin.pluginMeta.version,
                        editedCurrentVersionPrefix = data.editedCurrentVersionPrefix,
                        editedCurrentVersion = data.editedCurrentVersionPrefix.replace(
                            "<cutCurrentVersion>",
                            plugin.pluginMeta.version.replace("/[^0-9.]/".toRegex(), "")
                        ),
                        latestVersion = latestVersion.first,
                        editedLatestVersionPrefix = data.editedLatestVersionPrefix,
                        editedLatestVersion = data.editedLatestVersionPrefix.replace(
                            "<cutGetLatestVersion>",
                            latestVersion.first.replace("/[^0-9.]/".toRegex(), "")
                        ),
                        repositoryUrl = data.repositoryUrl,
                        autoInfoUpdate = data.autoInfoUpdate,
                        downloadIdentify = latestVersion.second,
                        latestUrlPrefix = data.latestUrlPrefix,
                        latestUrl = data.latestUrl,
                        install = data.install
                    )
                    newDataList.add(newData)
                }
            } else {
                newDataList.add(data)
            }
        }
        val newList = ManageList(newDataList)
        file.writeText(jsonFormant.encodeToString(newList))
        plugin.logger.info("Plugin information has been updated.")
    }

    @CommandMethod("url")
    fun generateUrl() {
        val file = plugin.dataFolder.resolve("manageList.json")
        if (!file.exists()) {
            plugin.logger.warning("manageList.json is not found")
            return
        }
        val list = jsonFormant.decodeFromString<ManageList>(file.readText())
        val newList = arrayListOf<PluginData>()
        list.list.forEach { data ->
            if (data.autoInfoUpdate) {
                val url = data.latestUrlPrefix
                    .replace("<editedLatestVersion>", data.editedLatestVersion)
                    .replace("<name>", data.name)
                    .replace("<identify>", data.identify)
                    .replace("<downloadIdentify>", data.downloadIdentify)
                val newData = PluginData(
                    identify = data.identify,
                    name = data.name,
                    currentVersion = data.currentVersion,
                    editedCurrentVersionPrefix = data.editedCurrentVersionPrefix,
                    editedCurrentVersion = data.editedCurrentVersion,
                    latestVersion = data.latestVersion,
                    editedLatestVersionPrefix = data.editedLatestVersionPrefix,
                    editedLatestVersion = data.editedLatestVersion,
                    downloadIdentify = data.downloadIdentify,
                    repositoryUrl = data.repositoryUrl,
                    autoInfoUpdate = data.autoInfoUpdate,
                    latestUrlPrefix = data.latestUrlPrefix,
                    latestUrl = url,
                    install = data.install
                )
                newList.add(newData)
            } else {
                newList.add(data)
            }
        }
        val newManageList = ManageList(newList)
        file.writeText(jsonFormant.encodeToString(newManageList))
        plugin.logger.info("URL has been generated.")
    }

    @CommandMethod("install")
    fun updateFile() {
        val file = plugin.dataFolder.resolve("manageList.json")
        if (!file.exists()) {
            plugin.logger.warning("manageList.json is not found")
            return
        }
        val list = jsonFormant.decodeFromString<ManageList>(file.readText())
        plugin.logger.info("${list.list.filter { it.install }.map { it.identify }.joinToString(" ,")} will be installed.")

        val error = arrayListOf<String>()

        list.list.forEach { data ->
            if (data.install) {
                val url = URL(data.latestUrl)
                try {
                    plugin.logger.info("Downloading ${data.name}...")
                    val pluginDirectory = plugin.dataFolder.resolve("plugin")
                    val downloadedFile = pluginDirectory.resolve("${data.name}_${data.editedLatestVersion}.jar")
                    pluginDirectory.mkdirs()
                    downloadedFile.createNewFile()
                    FileUtils.copyURLToFile(url, downloadedFile)
                    plugin.logger.info("${data.name} has been installed.")
                } catch (e: Exception) {
                   error.add(data.identify)
                }
            }
        }
        plugin.logger.info("${list.list.filter { it.install }.size - error.size} plugins have been installed.")
        if (error.isNotEmpty()) {
            plugin.logger.warning("${error.joinToString(" ,")} could not be installed.")
        }

    }


    private fun getLatestVersion(input: String): Pair<String, String> {
        if (input.contains("spigotmc.org")) {
            return getFromSpigot(input)
        }
        if (input.contains("modrinth.com")) {
            return getFromModrinth(input)
        }
        if (input.contains("hangar.papermc.io")) {
            return getFromHanger(input)
        }
        return Pair("", "")
    }

    private fun getFromSpigot(input: String): Pair<String, String> {
        var url = input
        if (url.last() == '/') {
            url = url.dropLast(1)
        }

        //object

        val resId = url.split(".")[(url.split(".").size - 1)]

        val reqUrl = URL("https://api.spiget.org/v2/resources/$resId/versions/latest")

        val con = reqUrl.openConnection() as HttpURLConnection
        con.connectTimeout = 20_000 // 20 秒
        con.readTimeout = 20_000    // 20 秒
        con.requestMethod = "GET"

        con.connect()

        val str = con.inputStream.bufferedReader(Charsets.UTF_8).use { br ->
            br.readLines().joinToString("")
        }

        val json = JSONObject(str)
        val version = json.getString("name")
        return Pair(version, "https://api.spiget.org/v2/resources/$resId/download")
    }

    private fun getFromModrinth(url: String): Pair<String, String> {

        val projectName = url.split("/")[4]

        val reqUrl = URL("https://api.modrinth.com/v2/project/$projectName/version")

        val con = reqUrl.openConnection() as HttpURLConnection
        con.connectTimeout = 20_000 // 20 秒
        con.readTimeout = 20_000    // 20 秒
        con.requestMethod = "GET"

        con.connect()

        val str = con.inputStream.bufferedReader(Charsets.UTF_8).use { br ->
            br.readLines().joinToString("")
        }

        val json = JSONArray(str)
        var version = ""
        var file = ""
        for (i in 0 until json.length()) {
            val data = json[i] as JSONObject
            if (data.getJSONArray("loaders").contains("paper")) {
                version = data.getString("version_number")
                file = data.getJSONArray("files").getJSONObject(0).getString("url")
                break
            }
        }
        return Pair(version, file)
    }

    private fun getFromHanger(url: String): Pair<String, String> {

        val owner = url.split("/")[
            3
        ]
        val projectName = url.split("/")[
            4
        ]

        val reqUrl = URL("https://hangar.papermc.io/api/v1/projects/$owner/$projectName/versions?limit=1&offset=0")

        val con = reqUrl.openConnection() as HttpURLConnection
        con.connectTimeout = 20_000 // 20 秒
        con.readTimeout = 20_000 // 20 秒
        con.requestMethod = "GET"

        con.connect()

        val str = con.inputStream.bufferedReader(Charsets.UTF_8).use { br ->
            br.readLines().joinToString("")
        }

        val json = JSONObject(str)
        val version = json.getJSONArray("result").getJSONObject(0).getString("name")
        var file = json.getJSONArray("result").getJSONObject(0).getJSONObject("downloads").getJSONObject("PAPER").get("downloadUrl").toString()
        if (file == "null") {
           file = json.getJSONArray("result").getJSONObject(0).getJSONObject("downloads").getJSONObject("PAPER"). getString("externalUrl")
        }
        return Pair(version, file)
    }
}
