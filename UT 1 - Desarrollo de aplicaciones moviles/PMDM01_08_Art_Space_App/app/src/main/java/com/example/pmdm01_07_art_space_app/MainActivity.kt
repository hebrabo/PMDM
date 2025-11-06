package com.example.pmdm01_07_art_space_app

import android.R.attr.fontWeight
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pmdm01_07_art_space_app.ui.theme.PMDM01_07_Art_Space_AppTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PMDM01_07_Art_Space_AppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ArtSpaceApp(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

data class Artwork(
    val imageRes: Int,
    val title: String,
    val artist: String,
    val year: String
)

@Composable
fun ArtSpaceApp(modifier: Modifier = Modifier) {
    var currentArtwork by remember { mutableStateOf(0) }

    val artworks = listOf(
        Artwork(R.drawable.munch_el_grito, "El grito", "Munch", "1893"),
        Artwork(R.drawable.vermeer_joven_de_la_perla, "La joven de la perla", "Vermeer", "1665"),
        Artwork(R.drawable.vangogh_la_noche_estrellada, "La noche estrellada", "Van Gogh", "1889")
    )
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Image(
            painter = painterResource(id = artworks[currentArtwork].imageRes),
            contentDescription = artworks[currentArtwork].title,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentScale = ContentScale.Fit
        )
        Text(text = artworks[currentArtwork].title, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text(text = artworks[currentArtwork].artist, fontSize = 18.sp, fontStyle = FontStyle.Italic)
        Text(text = artworks[currentArtwork].year, fontSize = 16.sp)

        Row (
            modifier = Modifier
                .padding(all = 16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Button(onClick = {
                if (currentArtwork > 0) currentArtwork--
            }
            ) {
                Text("Anterior")
            }
            Button(onClick = {
                if (currentArtwork < artworks.size -1) currentArtwork++
            }
            ) {
                Text("Siguiente")
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PMDM01_07_Art_Space_AppTheme {
        ArtSpaceApp()
    }
}