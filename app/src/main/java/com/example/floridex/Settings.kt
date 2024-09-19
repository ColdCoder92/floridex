package com.example.floridex

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.tooling.preview.Preview
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
        DescriptionArea(name)
        DescriptionTabButtons {
            TODO("not implemented")
        }
    }

    @Preview
    @Composable
    fun BackgroundTheme() {
        val color = if (isSystemInDarkTheme()) {
            listOf(DeepTeal80)
        } else {
            listOf(DeepTeal40)
        }

        Box(modifier = Modifier.fillMaxSize().background(color))
    }
}