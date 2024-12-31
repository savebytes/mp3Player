package com.example.mp3player.ui.viewModels

import android.media.MediaPlayer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.mp3player.core.CommonUtils.PlaybackState
import androidx.lifecycle.ViewModel
import com.example.mp3player.ui.helper.SongHelper
import com.example.mp3player.ui.helper.SongHelper.Companion
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.launch

class AudioPlayerViewModel : ViewModel() {
    val playbackState = mutableStateOf(PlaybackState.STOPPED)
    var isUpdating = mutableStateOf(true)


    companion object {
        var currentPosition = 0
        var mediaPlayer: MediaPlayer? = null
    }

    fun playSong(url:String) {
        playbackState.value = PlaybackState.LOADING
        isUpdating.value = false
        // Initialize MediaPlayer
        mediaPlayer = MediaPlayer().apply {
            setDataSource(url)
            setOnPreparedListener {
                playbackState.value = PlaybackState.PLAYING
                seekTo(AudioPlayerViewModel.currentPosition)
                start()
                isUpdating.value = true
            }
            setOnCompletionListener {
                playbackState.value = PlaybackState.STOPPED
                isUpdating.value = false
                AudioPlayerViewModel.currentPosition = 0
            }
            setOnErrorListener { _, _, _ ->
                playbackState.value = PlaybackState.STOPPED
                isUpdating.value = false
                true // Handle error
            }
            prepareAsync() // Load the media asynchronously
        }

    }

    fun pauseSong() {
        playbackState.value = PlaybackState.PAUSED
        isUpdating.value = false
        mediaPlayer?.let {
            AudioPlayerViewModel.currentPosition = it.currentPosition
            it.pause()
        }

    }

    fun stopSong() {
        mediaPlayer?.stop()
        playbackState.value = PlaybackState.STOPPED
        isUpdating.value = false
        mediaPlayer?.release()
        mediaPlayer = null
        AudioPlayerViewModel.currentPosition = 0
    }
     fun getDurationOfSong(): Long{
         return mediaPlayer?.duration!!.toLong()
     }

    override fun onCleared() {
        super.onCleared()
        mediaPlayer?.release()
        mediaPlayer = null
        currentPosition = 0
        isUpdating.value = false
    }
}