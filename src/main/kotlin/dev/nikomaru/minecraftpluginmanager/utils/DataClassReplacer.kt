/*
 * Written in 2023-2024 by Nikomaru <nikomaru@nikomaru.dev>
 *
 * To the extent possible under law, the author(s) have dedicated all copyright and related and neighboring rights to this software to the public domain worldwide.This software is distributed without any warranty.
 *
 * You should have received a copy of the CC0 Public Domain Dedication along with this software.
 * If not, see <http://creativecommons.org/publicdomain/zero/1.0/>.
 */

package dev.nikomaru.minecraftpluginmanager.utils

import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties


object DataClassReplacer {
    fun String.replaceTemplate(dataClass: Any): String {
        return replaceFields(this, dataClass, "")
    }

    private fun replaceFields(template: String, dataClass: Any, prefix: String): String {
        val fields = dataClass::class.memberProperties
        var replaced = template
        fields.forEach { field ->
            val name = if (prefix.isEmpty()) field.name else "$prefix.${field.name}"
            val value = (dataClass::class.members.find { it.name == field.name } as KProperty1<Any, *>).get(dataClass)
            replaced = if (isDataClass(value)) {
                replaceFields(replaced, value!!, name)
            } else {
                replaced.replace("<${name}>", value.toString())
            }
        }
        return replaced
    }

    private fun isDataClass(obj: Any?): Boolean {
        if (obj == null) return false
        return obj::class.isData
    }

}