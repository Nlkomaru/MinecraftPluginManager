package dev.nikomaru.minecraftpluginmanager.commands
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class Suggestion (val value:String)