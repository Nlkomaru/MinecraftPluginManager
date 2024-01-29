package dev.nikomaru.minecraftpluginmanager.utils

import dev.nikomaru.minecraftpluginmanager.data.PluginData
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import java.io.File

class PluginDataTest {
    @Test
    fun testLoadBukkitPluginData() {
        val bukkitPluginFile = File("src/test/resources/BukkitPlugin.jar")
        val bukkitPluginData = PluginDataUtils.getPluginData(bukkitPluginFile) as PluginData.BukkitPluginData
        assertNotNull("dev.nikomaru.minecraftpluginmanager.MinecraftPluginManager", bukkitPluginData.main)
    }

    @Test
    fun testLoadPaperPluginData() {
        //TODO 自前のプラグインに置き換える
        val paperPluginFile = File("src/test/resources/PaperPlugin.jar")
        val paperPluginData = PluginDataUtils.getPluginData(paperPluginFile) as PluginData.PaperPluginData
        assertEquals("1.20", paperPluginData.apiVersion)
    }
}