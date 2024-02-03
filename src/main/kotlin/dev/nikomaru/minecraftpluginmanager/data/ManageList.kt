package dev.nikomaru.minecraftpluginmanager.data

import kotlinx.serialization.Serializable

@Serializable
data class ManageList(val list: ArrayList<ManageData> = arrayListOf())



