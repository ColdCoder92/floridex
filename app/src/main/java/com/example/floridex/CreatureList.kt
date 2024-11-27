package com.example.floridex

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import com.example.floridex.ui.theme.DeepTeal40
import com.example.floridex.ui.theme.Green40
import com.example.floridex.ui.theme.Orange40

data class Item(val name: String, val imageRes: Int, val description: String)

class CreatureList {
    private val items = listOf(
        Item("Cat", R.drawable.cat, "One of the most adorable beings on the planet."),
        Item("Dog", R.drawable.dog, "Loyal companions that love unconditionally."),
        Item("Bird", R.drawable.bird, "Beautiful creatures that bring joy with their songs.")
    )

    @Composable
    fun MakeCreatureList(context: Context) {
        BackgroundTheme()
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(items.size) { index ->
                DescriptionItem(item = items[index])
            }
        }
    }

    @Composable
    fun BackgroundTheme() {
        val colors = listOf(Orange40, Green40, DeepTeal40)
        Box(modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors,
                    Offset(0f, 0f),
                    Offset(750f, 0f),
                    TileMode.Clamp
                )
            ))
    }

    @Composable
    fun DescriptionItem(item: Item) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .background(Green40)
                .padding(16.dp)
        ) {
            Text(item.name, textAlign = TextAlign.Center)
            Image(
                painter = painterResource(id = item.imageRes),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )
            Text(item.description, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Preview
@Composable
fun PreviewDescription() {
    val list = CreatureList()
    list.MakeCreatureList(context = LocalContext.current)
}
