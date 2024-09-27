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

class Settings {
    val Context.screenWidth: Int
        get() = resources.displayMetrics.widthPixels

    val Context.screenHeight: Int
        get() = resources.displayMetrics.heightPixels

    @Composable
    fun RunSettings(name: String, modifier: Modifier) {
        BackgroundTheme()
    }

    @Preview
    @Composable
    fun BackgroundTheme() {
        var color = DeepTeal40
        if (isSystemInDarkTheme())
        {
            color = DeepTeal80
        }

        Box (modifier = Modifier.fillMaxSize().background(color = color))

        Box (modifier = Modifier.width(425.dp).heightIn(75.dp).background(Green40))
        {
            Text("Settings", modifier = Modifier.offset(167.5.dp, 30.dp), textAlign = TextAlign.Center)
        }

        Box (modifier = Modifier.offset(0.dp, 100.dp).width(425.dp).heightIn(50.dp).background(Orange40))
        {
            Text("Username", modifier = Modifier.offset(5.dp, 17.5.dp))

            Image(painter = painterResource(R.drawable.edit_icon), contentDescription = null, modifier = Modifier.offset(280.dp, 6.5.dp))

            Text("Username", modifier = Modifier.offset(320.dp, 17.5.dp), textAlign = TextAlign.End)
            // ^ Trying to get this aligned to the right
        }

        Box (modifier = Modifier.offset(0.dp, 151.dp).width(425.dp).heightIn(50.dp).background(Orange40))
        {
            Text("Reset Password", modifier = Modifier.offset(5.dp, 17.5.dp))
        }

        Box (modifier = Modifier.offset(0.dp, 202.dp).width(425.dp).heightIn(50.dp).background(Orange40))
        {
            Text("Contact Support", modifier = Modifier.offset(5.dp, 17.5.dp))
        }

    }

    @Composable
    fun SettingsButtons(onClick: () -> Unit) {

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

    /*
    @Composable
    fun SettingsArea()
    {

    }

    */

    // Took some code from Description.kt (I believe was Lucas' work)
}