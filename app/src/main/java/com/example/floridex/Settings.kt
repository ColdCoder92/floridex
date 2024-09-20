package com.example.floridex

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
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
import com.example.floridex.ui.theme.DeepTeal40
import com.example.floridex.ui.theme.DeepTeal80
import com.example.floridex.ui.theme.Green40
import com.example.floridex.ui.theme.Green80
import com.example.floridex.ui.theme.Orange40
import com.example.floridex.ui.theme.Orange80

class Settings {
    val Context.screenWidth: Int
        get() = resources.displayMetrics.widthPixels

    val Context.screenHeight: Int
        get() = resources.displayMetrics.heightPixels

    @Composable
    fun MakeDescription(name: String, modifier: Modifier) {
        BackgroundTheme()
    }

    @Preview
    @Composable
    fun BackgroundTheme() {
        var color = listOf(DeepTeal40)
        if (isSystemInDarkTheme())
        {
            color = listOf(DeepTeal80)
        }

        Box (modifier = Modifier.fillMaxSize().background(color))
        {
            // To fill out soon
        }

        /*
        @Composable
        fun SettingsArea() {

        }
        */
    }
    // Took some code from MainActivity.kt (I believe was Lucas' work)
}