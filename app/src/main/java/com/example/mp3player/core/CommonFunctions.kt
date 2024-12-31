package com.example.mp3player.core

object CommonFunctions {

    fun durationToMilliseconds(duration: String): Long {
        val parts = duration.split(":")
        if (parts.size != 2) {
            throw IllegalArgumentException("Invalid duration format. Use 'MM:SS'")
        }
        val minutes = parts[0].toIntOrNull() ?: throw NumberFormatException("Invalid minutes value")
        val seconds = parts[1].toIntOrNull() ?: throw NumberFormatException("Invalid seconds value")

        return (minutes * 60 + seconds) * 1000L
    }

}