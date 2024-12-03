package com.example.floridex

import android.R.attr.screenSize
import android.R.attr.start
import android.content.Context
import android.content.res.Resources
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.floridex.ui.theme.DeepTeal40
import com.example.floridex.ui.theme.DeepTeal80
import com.example.floridex.ui.theme.Green40
import com.example.floridex.ui.theme.Green80
import com.example.floridex.ui.theme.Orange40
import com.example.floridex.ui.theme.Orange80
import com.example.floridex.Description
import com.example.floridex.CreatureList
import com.example.floridex.Settings


import androidx.compose.ui.tooling.preview.Preview
import com.example.floridex.ui.theme.FloridexTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FloridexTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val description = Description()
                    val settings = Settings()
                    val appContext = applicationContext
                    val inMenu: MutableState<Boolean> = remember { mutableStateOf(true) }
                    val dexID: MutableState<Int> = remember { mutableStateOf(0) } // Can be modified dynamically

                    val onItemSelected: (Int) -> Unit = { id ->
                        dexID.value = id
                        inMenu.value = false // Hide the menu when an item is selected
                    }

                    if(inMenu.value) {
                        val creatureList = CreatureList()
                        creatureList.MakeCreatureList(appContext, onItemSelected)
                    } else {
                        description.MakeDescription(modifier = Modifier.padding(innerPadding),
                            appContext,
                            dexID = dexID.value
                        )
                    }


//                    settings.MakeSettingsMenu(modifier = Modifier.padding(innerPadding), appContext)
                }
            }
        }
    }
}
