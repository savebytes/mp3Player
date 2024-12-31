package com.example.mp3player.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mp3player.data.models.Song
import com.example.mp3player.data.models.songsList
import com.example.mp3player.downloader.AndroidDownloader

@Composable
fun SongsList(
    songsList: List<Song>,
    onSongSelected: (song: Song) -> Unit,
    downloader: AndroidDownloader,
) {
    var isSongSelected by remember { mutableStateOf(false) }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                bottom = if (isSongSelected) {
                    100.dp
                } else {
                    100.dp
                }
            )
    ) {
        items(songsList) { song ->
            SongCard(song = song, onClick = {

                /*if(isSongSelected){
                    //SongHelper.pauseStream()
                    isSongSelected = false
                } else {
                    //SongHelper.playStream(song.media)
                    isSongSelected = true
                }*/
                isSongSelected = true
                onSongSelected(song)

            }, onDownloadClick = {
                downloader.downloadFile(song.media)
            })
        }
    }
}

/*
@Preview(showBackground = true)
@Composable
fun SongsListPreview() {
    SongsList(songsList = songsList, onSongSelected = {}, downloader = AndroidDownloader())
}*/
