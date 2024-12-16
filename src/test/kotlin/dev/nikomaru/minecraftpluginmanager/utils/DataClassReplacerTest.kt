package dev.nikomaru.minecraftpluginmanager.utils

import dev.nikomaru.minecraftpluginmanager.utils.DataClassReplacer.replaceTemplate
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
