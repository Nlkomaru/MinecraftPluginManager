package dev.nikomaru.minecraftpluginmanager.commands

import dev.nikomaru.minecraftpluginmanager.MinecraftPluginManager
import dev.nikomaru.minecraftpluginmanager.MinecraftPluginManager.Companion.plugin
import dev.nikomaru.minecraftpluginmanager.data.ManageList
import dev.nikomaru.minecraftpluginmanager.data.PluginData
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.apache.commons.io.FileUtils
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.json.JSONArray
import org.json.JSONObject
import revxrsal.commands.annotation.Command
import revxrsal.commands.annotation.Description
import revxrsal.commands.annotation.Subcommand
import revxrsal.commands.bukkit.annotation.CommandPermission
import java.net.HttpURLConnection
import java.net.URL


@Command("mpm")
@CommandPermission("mpm.command")
class Command {


    companion object {
        const val SPIGOT_URL = "spigotmc.org"
        const val MODRINTH_URL = "modrinth.com"
        const val HANGER_URL = "hangar.papermc.io"
        val jsonFormant = Json {
            prettyPrint = true
            encodeDefaults = true
        }
    }

    @Subcommand("init")
    @Description("初期化します。")
    fun create(sender: CommandSender) {
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
        list.list.forEach {
            sender.sendMessage("${it.name} : ${it.currentVersion}")
        }
        sender.sendRichMessage("${list.list.size} plugins have been registered.")
        sender.sendRichMessage("manageList.json is created")
    }

    @Subcommand("info")
    @Description("プラグインの情報を更新します。")
    fun updateInfo(sender: CommandSender) {
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
                            "<cutCurrentVersion>", plugin.pluginMeta.version.replace("/[^0-9.]/".toRegex(), "")
                        ),
                        latestVersion = latestVersion.first,
                        editedLatestVersionPrefix = data.editedLatestVersionPrefix,
                        editedLatestVersion = data.editedLatestVersionPrefix.replace(
                            "<cutGetLatestVersion>", latestVersion.first.replace("/[^0-9.]/".toRegex(), "")
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
        sender.sendRichMessage("Plugin information has been updated.")
    }

    @Subcommand("repository add")
    @Description("プラグインのリポジトリを追加します。")
    fun repo(sender: CommandSender, @Suggestion("identify") identify: String, url: String) {
        if (!url.contains(SPIGOT_URL) && !url.contains(MODRINTH_URL) && !url.contains(HANGER_URL)) {
            sender.sendRichMessage("This repository is not supported.")
            return
        }
        val file = plugin.dataFolder.resolve("manageList.json")
        if (!file.exists()) {
            plugin.logger.warning("manageList.json is not found")
            return
        }
        val list = jsonFormant.decodeFromString<ManageList>(file.readText())
        val data = list.list.find { it.identify == identify }
        if (data == null) {
            sender.sendRichMessage("This plugin is not found.")
            return
        }
        val newData = data.copy(repositoryUrl = url)
        list.list.remove(data)
        list.list.add(newData)
        file.writeText(jsonFormant.encodeToString(list))
        sender.sendRichMessage("Repository has been changed.")
    }

    @Subcommand("download add")
    @Description("プラグインのダウンロード識別子を追加します。")
    fun download(sender: CommandSender, @Suggestion("identify") identify: String, downloadIdentify: String) {
        val file = plugin.dataFolder.resolve("manageList.json")
        if (!file.exists()) {
            plugin.logger.warning("manageList.json is not found")
            return
        }
        val list = jsonFormant.decodeFromString<ManageList>(file.readText())
        val data = list.list.find { it.identify == identify }
        if (data == null) {
            sender.sendRichMessage("This plugin is not found.")
            return
        }
        val newData = data.copy(downloadIdentify = downloadIdentify)
        list.list.remove(data)
        list.list.add(newData)
        file.writeText(jsonFormant.encodeToString(list))
        sender.sendRichMessage("Download identify has been changed.")
    }

    @Subcommand("show")
    @Description("プラグインの情報を表示します。")
    fun showAll(sender: CommandSender, page: Int = 1) {
        val file = plugin.dataFolder.resolve("manageList.json")
        if (!file.exists()) {
            plugin.logger.warning("manageList.json is not found")
            return
        }
        val original = jsonFormant.decodeFromString<ManageList>(file.readText()).list
        val list = original.subList((page - 1) * 5, page * 5)
        list.forEach {
            val message = """
                name : ${it.identify}
                repositoryUrl : ${it.repositoryUrl} <click:suggest_command:'/mpm repository add'>[クリックで変更]</click>
                downloadIdentify : ${it.repositoryUrl}<click:suggest_command:'/mpm download add'>[クリックで変更]</click>
                autoInfoUpdate : <click:run_command:'/mpm autoIndoUpdate toggle'>${it.identify}>${if (it.autoInfoUpdate) "<color:red>[クリックで無効]" else "<color:green>[クリックで有効]"}</click>
                install : <click:run_command:'/mpm install toggle'>${if (it.install) "<color:red>[クリックで無効]" else "<color:green>[クリックで有効]"}</click>
            """.trimIndent()
            sender.sendRichMessage(message)
        }
        sender.sendRichMessage("page : $page/${original.size / 5 + 1}")
        if (page != 1) sender.sendRichMessage("<click:run_command:'/mpm show ${page - 1}'>[前のページへ]</click>")
        if (page != original.size / 5 + 1) sender.sendRichMessage("<click:run_command:'/mpm show ${page + 1}'>[次のページへ]</click>")
    }

    @Subcommand("autoInfoUpdate toggle")
    @Description("プラグインの情報更新を有効化/無効化します。")
    fun autoInfoUpdateToggle(sender: CommandSender, @Suggestion("identify") identify: String) {
        val file = plugin.dataFolder.resolve("manageList.json")
        if (!file.exists()) {
            plugin.logger.warning("manageList.json is not found")
            return
        }
        val list = jsonFormant.decodeFromString<ManageList>(file.readText())
        val data = list.list.find { it.identify == identify }
        if (data == null) {
            sender.sendRichMessage("This plugin is not found.")
            return
        }
        val newData = data.copy(autoInfoUpdate = !data.autoInfoUpdate)
        list.list.remove(data)
        list.list.add(newData)
        file.writeText(jsonFormant.encodeToString(list))
        sender.sendRichMessage("AutoInfoUpdate has been changed.")
    }

    @Subcommand("install toggle")
    @Description("プラグインのインストールを有効化/無効化します。")
    fun installToggle(sender: CommandSender, @Suggestion("identify") identify: String) {
        val file = plugin.dataFolder.resolve("manageList.json")
        if (!file.exists()) {
            plugin.logger.warning("manageList.json is not found")
            return
        }
        val list = jsonFormant.decodeFromString<ManageList>(file.readText())
        val data = list.list.find { it.identify == identify }
        if (data == null) {
            sender.sendRichMessage("This plugin is not found.")
            return
        }
        val newData = data.copy(install = !data.install)
        list.list.remove(data)
        list.list.add(newData)
        file.writeText(jsonFormant.encodeToString(list))
        sender.sendRichMessage("Install has been changed.")
    }


    @Subcommand("version list")
    @Description("プラグインのバージョンを表示します。")
    fun versionList(sender: CommandSender) {
        val file = plugin.dataFolder.resolve("manageList.json")
        if (!file.exists()) {
            plugin.logger.warning("manageList.json is not found")
            return
        }
        val list = jsonFormant.decodeFromString<ManageList>(file.readText())
        list.list.forEach {
            if (it.editedLatestVersion != it.editedCurrentVersion) {
                sender.sendMessage("${it.name} : <red>${it.editedCurrentVersion}</red> -> <green>${it.editedLatestVersion}</green>")
            } else {
                sender.sendMessage("${it.name} : <green>${it.editedCurrentVersion}</green>")
            }
        }
    }

    @Subcommand("url")
    @Description("プラグインのURLを生成します。")
    fun generateUrl(sender: CommandSender) {
        val file = plugin.dataFolder.resolve("manageList.json")
        if (!file.exists()) {
            plugin.logger.warning("manageList.json is not found")
            return
        }
        val list = jsonFormant.decodeFromString<ManageList>(file.readText())
        val newList = arrayListOf<PluginData>()
        list.list.forEach { data ->
            if (data.autoInfoUpdate) {
                val url = data.latestUrlPrefix.replace("<editedLatestVersion>", data.editedLatestVersion)
                    .replace("<name>", data.name).replace("<identify>", data.identify)
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
        sender.sendRichMessage("URL has been generated.")
    }

    @Subcommand("install")
    @Description("プラグインをインストールします。")
    fun updateFile(sender: CommandSender) {
        val file = plugin.dataFolder.resolve("manageList.json")
        if (!file.exists()) {
            plugin.logger.warning("manageList.json is not found")
            return
        }
        val list = jsonFormant.decodeFromString<ManageList>(file.readText())
        sender.sendMessage("${
            list.list.filter { it.install && it.editedCurrentVersion != it.editedLatestVersion }
                .joinToString(" ,") { it.identify }
        } will be installed.")

        val error = arrayListOf<String>()

        list.list.forEach { data ->
            if (data.install && data.editedCurrentVersion != data.editedLatestVersion) {
                val url = URL(data.latestUrl)
                try {
                    sender.sendRichMessage("Downloading ${data.name}...")
                    val pluginDirectory = plugin.dataFolder.resolve("plugin")
                    val downloadedFile = pluginDirectory.resolve("${data.name}_${data.editedLatestVersion}.jar")
                    pluginDirectory.mkdirs()
                    downloadedFile.createNewFile()
                    FileUtils.copyURLToFile(url, downloadedFile)
                    sender.sendRichMessage("${data.name} has been installed.")
                } catch (e: Exception) {
                    error.add(data.identify)
                }
            }
        }
        sender.sendRichMessage("${list.list.filter { it.install && it.editedCurrentVersion != it.editedLatestVersion }.size - error.size} plugins have been installed.")
        if (error.isNotEmpty()) {
            plugin.logger.warning("${error.joinToString(" ,")} could not be installed.")
        }

    }


    private fun getLatestVersion(input: String): Pair<String, String> {
        if (input.contains(SPIGOT_URL)) {
            return getFromSpigot(input)
        }
        if (input.contains(MODRINTH_URL)) {
            return getFromModrinth(input)
        }
        if (input.contains(HANGER_URL)) {
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

        val owner = url.split("/")[3]
        val projectName = url.split("/")[4]

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
        var file = json.getJSONArray("result").getJSONObject(0).getJSONObject("downloads").getJSONObject("PAPER")
            .get("downloadUrl").toString()
        if (file == "null") {
            file = json.getJSONArray("result").getJSONObject(0).getJSONObject("downloads").getJSONObject("PAPER")
                .getString("externalUrl")
        }
        return Pair(version, file)
    }
}
