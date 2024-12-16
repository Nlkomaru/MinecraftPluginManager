/*
 * Written in 2023-2024 by Nikomaru <nikomaru@nikomaru.dev>
 *
 * To the extent possible under law, the author(s) have dedicated all copyright and related and neighboring rights to this software to the public domain worldwide.This software is distributed without any warranty.
 *
 * You should have received a copy of the CC0 Public Domain Dedication along with this software.
 * If not, see <http://creativecommons.org/publicdomain/zero/1.0/>.
 */

package dev.nikomaru.minecraftpluginmanager.utils

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer

object ComponentUtils {
    fun Component.toLegacyText(): String {
        return LegacyComponentSerializer.builder().build().serialize(this)
    }

    fun Component.toGsonText(): String {
        return GsonComponentSerializer.gson().serialize(this)
    }

    fun String.toComponent(): Component {
        return MiniMessage.miniMessage().deserialize(this)
    }

}