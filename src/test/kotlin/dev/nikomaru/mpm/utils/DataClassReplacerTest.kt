/*
 * Written in 2023-2025 by Nikomaru <nikomaru@nikomaru.dev>
 *
 * To the extent possible under law, the author(s) have dedicated all copyright and related and neighboring rights to this software to the public domain worldwide.This software is distributed without any warranty.
 *
 * You should have received a copy of the CC0 Public Domain Dedication along with this software.
 * If not, see <http://creativecommons.org/publicdomain/zero/1.0/>.
 */

package dev.nikomaru.mpm.utils

import dev.nikomaru.mpm.utils.DataClassReplacer.replaceTemplate
import org.junit.jupiter.api.Assertions.assertEquals
import org.koin.test.KoinTest

class DataClassReplacerTest: KoinTest {
    @org.junit.jupiter.api.Test
    fun replace() {
        val data = "Hello, <name>! You are <age> years old."
        val person = Person("Nikomaru", 18)
        val replaced = data.replaceTemplate(person)
        assertEquals("Hello, Nikomaru! You are 18 years old.", replaced)
    }
}

data class Person(val name: String, val age: Int, val work: Work = Work("Programmer", 100000))
data class Work(val name: String, val salary: Int)
