package com.example.mp3player

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.mp3player.data.models.songsList
import com.example.mp3player.downloader.AndroidDownloader
import com.example.mp3player.ui.screens.HomeScreen
import com.example.mp3player.ui.theme.Mp3PlayerTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val downloader = AndroidDownloader(this)
        setContent {
            Mp3PlayerTheme {
                Scaffold(topBar = {
                    TopAppBar(
                        title = { Text(text = "Mp3 Player") },
                        modifier = Modifier.background(Color.Blue),
                    )
                }) { innerPadding ->
                    HomeScreen(songsList = songsList,innerPadding, downloader)
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Mp3PlayerTheme {
        Greeting("Android")
    }
}