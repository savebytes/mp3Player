package com.example.mp3player.ui.screens

import android.util.Log
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Loop
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.mp3player.core.CommonFunctions.durationToMilliseconds
import com.example.mp3player.core.CommonUtils
import com.example.mp3player.ui.helper.SongHelper
import com.example.mp3player.data.models.Song
import com.example.mp3player.data.models.songsList
import com.example.mp3player.ui.viewModels.AudioPlayerViewModel
import com.example.mp3player.ui.viewModels.AudioPlayerViewModel.Companion.currentPosition
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MediaPlayerCard(modifier: Modifier = Modifier, song: Song) {
    var songState by remember { mutableStateOf(false) }
    val viewModel = remember { AudioPlayerViewModel() }

    val scope = rememberCoroutineScope()
    val playbackState = viewModel.playbackState.value
    var progress by remember { mutableFloatStateOf(AudioPlayerViewModel.currentPosition.toFloat()) }
    var isUpdating by remember { mutableStateOf(true) }
    val animatedProgress = animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(
            durationMillis = 500, // Smooth out update every 500ms
            easing = LinearEasing
        )
    ).value


    LaunchedEffect(key1 = isUpdating) {
        scope.launch {
            val steps = 100 // Number of updates for the animation
            val stepDelay = durationToMilliseconds(song.duration) / steps

            for (i in 0..steps) {
                progress = i / steps.toFloat()
                delay(stepDelay)
                if (!isUpdating) break
            }
        }
    }

    Card(modifier = modifier,) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp, 15.dp),
        ) {
            AsyncImage(
                model = song.imageUrl,
                contentDescription = "Song thumbnail",
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(4.dp))
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = song.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = song.artist,
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Icon(
                imageVector = if (CommonUtils.PlaybackState.PLAYING == playbackState) {
                    Icons.Filled.Pause
                }else if(CommonUtils.PlaybackState.LOADING == playbackState){
                    Icons.Filled.Loop
                } else {
                    Icons.Filled.PlayArrow
                },
                contentDescription = "Play/Pause",
                modifier = Modifier
                    .size(30.dp)
                    .clickable {
                        when (playbackState) {
                            CommonUtils.PlaybackState.LOADING -> {
                                Log.d("TAG", "MediaPlayerCard: LOADING...")
                                isUpdating = true
                            }

                            CommonUtils.PlaybackState.PLAYING -> {
                                viewModel.pauseSong()
                                isUpdating = false
                                Log.d("TAG", "MediaPlayerCard: PLAYING...")

                            }

                            CommonUtils.PlaybackState.PAUSED -> {
                                viewModel.playSong(song.media)
                                isUpdating = true
                                Log.d("TAG", "MediaPlayerCard: PAUSED...")

                            }

                            CommonUtils.PlaybackState.STOPPED -> {
                                viewModel.playSong(song.media)
                                isUpdating = true
                                Log.d("TAG", "MediaPlayerCard: STOPPED...")

                            }
                        }
                        Log.d("TAG", "MediaPlayerCard: ${AudioPlayerViewModel.currentPosition}")
                        Log.d("TAG", "MediaPlayerCard: IS UPDATING $isUpdating")
                    })
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (CommonUtils.PlaybackState.PLAYING == playbackState){
                LinearProgressIndicator(
                    progress = { animatedProgress },
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            } else {
                LinearProgressIndicator(
                    progress = { animatedProgress },
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            }
        }
    }

    /*DisposableEffect(song.media){
        onDispose {
            songState = false
            SongHelper.releasePlayer()
        }
    }*/

}

@Preview(showBackground = true)
@Composable
fun MediaPlayerCardPreview() {
    MediaPlayerCard(
        song = songsList[0],
    )
}