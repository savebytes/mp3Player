package com.example.mp3player.downloader

interface Downloader {
    fun downloadFile(url: String): Long
}