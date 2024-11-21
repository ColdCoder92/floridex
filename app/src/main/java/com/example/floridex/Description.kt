package com.example.floridex

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Picture
import android.widget.ImageView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.floridex.ui.theme.DeepTeal40
import com.example.floridex.ui.theme.DeepTeal80
import com.example.floridex.ui.theme.Green40
import com.example.floridex.ui.theme.Green80
import com.example.floridex.ui.theme.Orange40
import com.example.floridex.ui.theme.Orange80

class Description {

    val Context.screenWidth: Int
        get() = resources.displayMetrics.widthPixels

    val Context.screenHeight: Int
        get() = resources.displayMetrics.heightPixels

    @Composable
    fun MakeDescription(name: String, modifier: Modifier) {
        BackgroundTheme()
        DescriptionArea(name)
        DescriptionTabButtons {
            TODO("not implemented")
        }
    }

    @Preview
    @Composable
    fun BackgroundTheme() {
        val colors = if (isSystemInDarkTheme()) {
            listOf(Orange80, Green80, DeepTeal80)
        } else {
            listOf(Orange40, Green40, DeepTeal40)
        }

        Box(modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors,
                    Offset(0f, 0f),
                    Offset(750f, 0f),

                    TileMode.Clamp)))
    }

    @Composable
    fun DescriptionTabButtons(onClick: () -> Unit) {
        Button(colors = ButtonColors(DeepTeal40, Color.White, DeepTeal40, Color.Black),
            modifier = Modifier.offset(0.dp, 100.dp).width(137.5f.dp),
            onClick = { onClick() }) {
            Text("Info")
        }
        Button(colors = ButtonColors(DeepTeal40, Color.White, DeepTeal40, Color.Black),
            modifier = Modifier.offset(137.5f.dp, 100.dp).width(137.5f.dp),
            onClick = { onClick() }) {
            Text("Cry")
        }
        Button(colors = ButtonColors(DeepTeal40, Color.White, DeepTeal40, Color.Black),
            modifier = Modifier.offset(275.dp, 100.dp).width(137.5f.dp),
            onClick = { onClick() }) {
            Text("Map")
        }
    }

    @Composable
    fun DescriptionArea(name: String) {
        Box (modifier = Modifier.offset(0.dp, 125.dp).width(425.dp).heightIn(250.dp)
            .fillMaxSize().background(Green40)) {
            Text(name, modifier = Modifier.offset(0.dp, 50.dp), textAlign = TextAlign.Center)
            Image(painter = painterResource(R.drawable.cat), contentDescription = null,
                modifier = Modifier.offset(0.dp, 100.dp),
                contentScale = ContentScale.FillWidth
            )
            Text("XXX Kilograms", Modifier.offset(200.dp, 100.dp))
            Text("X Meters", Modifier.offset(200.dp, 125.dp))
            Text(
                "One of the most adorable beings on the planet, cats wander around and search for food like their ancestors. They formed a bond with humans since ancient times.",
                Modifier.offset(0.dp, 250.dp)
            )
        }
    }
}