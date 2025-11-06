package com.example.pmdm01_07_art_space_app

import android.R.attr.fontWeight
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pmdm01_07_art_space_app.ui.theme.PMDM01_07_Art_Space_AppTheme
import androidx.compose.ui.graphics.Color


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


    val artworks = listOf(
        Artwork(R.drawable.munch_el_grito, "El grito", "Munch", "1893"),
        Artwork(R.drawable.vermeer_joven_de_la_perla, "La joven de la perla", "Vermeer", "1665"),
        Artwork(R.drawable.vangogh_la_noche_estrellada, "La noche estrellada", "Van Gogh", "1889")
    )

    var currentArtwork by remember { mutableStateOf<Artwork>(artworks[0]) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Image(
            painter = painterResource(id = currentArtwork.imageRes),
            contentDescription = currentArtwork.title,
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.padding(vertical = 16.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFBBDEFB)) // fondo gris
                .padding(8.dp), // padding interno para separar el texto del fondo
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = currentArtwork.title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = currentArtwork.artist,
                fontSize = 18.sp,
                fontStyle = FontStyle.Italic,
                color = Color.DarkGray
            )
            Text(
                text = currentArtwork.year,
                fontSize = 16.sp,
                color = Color.DarkGray
            )
        }

        Spacer(modifier = Modifier.height(16.dp))


        Row (
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Button(onClick = {
                val currentIndex = artworks.indexOf(currentArtwork)
                currentArtwork = if (currentIndex == 0) artworks.last() else artworks[currentIndex - 1]
            }) { Text("Anterior") }

            Button(onClick = {
                val currentIndex = artworks.indexOf(currentArtwork)
                currentArtwork = if (currentIndex == artworks.lastIndex) artworks.first() else artworks[currentIndex + 1]
            }) { Text("Siguiente") }
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