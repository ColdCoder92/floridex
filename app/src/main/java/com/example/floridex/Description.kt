package com.example.floridex

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageFormat
import android.graphics.Picture
import android.media.ImageReader
import android.media.MediaRecorder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
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

import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.MutableLiveData
import com.android.volley.AuthFailureError
import com.android.volley.NetworkError
import com.android.volley.NoConnectionError
import com.android.volley.ParseError
import com.android.volley.ServerError
import com.android.volley.TimeoutError
import org.json.JSONArray
import org.json.JSONObject
import com.google.gson.Gson
import com.google.gson.GsonBuilder

data class Creature(
    val dexID: Int,
    val name: String,
    val habitat: String,
    val description: String,
    val weight: Double,
    val height: Double,
    val image: JSONObject,
    val type: String,
    val author: String
)

class Description {
    private lateinit var requestQueue: RequestQueue
    private lateinit var textView: TextView
    private val gatewayLINK = "https://id5sdg2r34.execute-api.us-east-1.amazonaws.com/filter"

    val Context.screenWidth: Int
        get() = resources.displayMetrics.widthPixels

    val Context.screenHeight: Int
        get() = resources.displayMetrics.heightPixels

    @RequiresApi(Build.VERSION_CODES.Q)
    @Composable
    fun MakeDescription(modifier: Modifier, context: Context, dexID: Int) {
        requestQueue = Volley.newRequestQueue(context)
        textView = TextView(context)
        val hasResponse = remember { mutableStateOf(false) }
        var responseInfo = remember { mutableStateOf(JSONObject()) }

        val stringRequest = StringRequest(
            Request.Method.GET,
            ("$gatewayLINK?id=$dexID"),
            { response ->
                textView.text = response
                responseInfo.value = JSONObject(response)
                hasResponse.value = true
            },
            { error ->
                textView.text = when (error) {
                    is TimeoutError -> "Request timed out"
                    is NoConnectionError -> "No internet connection"
                    is AuthFailureError -> "Authentication error"
                    is ServerError -> "Server error"
                    is NetworkError -> "Network error"
                    is ParseError -> "Data parsing error"
                    else -> "Error: ${error.message}"
                }
            }
        )

        requestQueue.add(stringRequest)
        if (hasResponse.value) {
            val gson = Gson()
            val rowValue = responseInfo.value.get("wildlife")
            val creatures: List<Creature> = gson.fromJson(
                rowValue.toString(), Array<Creature>::class.java
            ).toList()

            println("Response: ${creatures[0].image}")
            BackgroundTheme()
            ProfileNav()
            DescriptionArea(creatures[0])
            DescriptionTabButtons(creatures[0])
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

                    TileMode.Clamp
                )
            ))
    }

    @Composable
    fun ProfileNav() {
        Button(modifier = Modifier.offset(350.dp, 37.5.dp).width(50.dp).height(50.dp),
            onClick = {}
        ) {}
        Image(painter = painterResource(R.drawable.profile), contentDescription = null,
            modifier = Modifier.offset(350.dp, 37.5.dp).width(50.dp).height(50.dp)
        )
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    @Composable
    fun DescriptionTabButtons(creature: Creature) {
        var infoPressed = remember { mutableStateOf(false) }
        var cryPressed = remember { mutableStateOf(false) }
        var mapPressed = remember { mutableStateOf(false) }

        Button(colors = ButtonColors(DeepTeal40, Color.White, DeepTeal40, Color.Black),
            modifier = Modifier
                .offset(0.dp, 100.dp)
                .width(137.5f.dp),
            onClick = {
                println("Info pressed")
                infoPressed.value = true
                cryPressed.value = false
                mapPressed.value = false
            }) {
            Text("Info")
        }
        Button(colors = ButtonColors(DeepTeal40, Color.White, DeepTeal40, Color.Black),
            modifier = Modifier
                .offset(137.5f.dp, 100.dp)
                .width(137.5f.dp),
            onClick = {
                println("Cry pressed")
                cryPressed.value = true
                infoPressed.value = false
                mapPressed.value = false
            }) {
            Text("Cry")
        }
        Button(colors = ButtonColors(DeepTeal40, Color.White, DeepTeal40, Color.Black),
            modifier = Modifier
                .offset(275.dp, 100.dp)
                .width(137.5f.dp),
            onClick = {
                println("Map pressed")
                mapPressed.value = true
                infoPressed.value = false
                cryPressed.value = false
            }) {
            Text("Map")
        }

        if (infoPressed.value) {
            println("Info page")
            DescriptionArea(creature)
            DescriptionTabButtons(creature)
        }

        if (cryPressed.value) {
            println("Cry page")
            CryArea(creature)
            DescriptionTabButtons(creature)
        }

        if (mapPressed.value) {
            println("Map page")
            MapArea(creature)
            DescriptionTabButtons(creature)
        }
    }

    @Composable
    fun DescriptionArea(creature: Creature) {
        val name = creature.name
        val habitat = creature.habitat
        val weight = creature.weight
        val height = creature.height
        val description = creature.description
        val imageData = creature.image.get("data") as ByteArray

        val imageReader = ImageReader.newInstance(50, 50, ImageFormat.JPEG, 1)
        var imageBitmap: ImageBitmap = ImageBitmap(50, 50)

        // Obtain the image from the ImageReader
        val image = imageReader.acquireLatestImage()
        image?.let {
            val buffer = image.planes[0].buffer
            buffer.put(imageData)
            val bitmap = Bitmap.createBitmap(50, 50, Bitmap.Config.ARGB_8888)
            bitmap.copyPixelsFromBuffer(buffer)
            imageBitmap = bitmap.asImageBitmap()
            image.close()
        }


        Box (modifier = Modifier
            .offset(0.dp, 125.dp)
            .width(425.dp)
            .heightIn(250.dp)
            .fillMaxSize()
            .background(Green40)) {
            Text(name, modifier = Modifier.offset(0.dp, 50.dp), textAlign = TextAlign.Center)
            Image(bitmap = imageBitmap, contentDescription = null,
                modifier = Modifier.offset(0.dp, 100.dp),
                contentScale = ContentScale.FillWidth
            )
            Text("$weight Kilograms", Modifier.offset(200.dp, 100.dp))
            Text("$height Meters", Modifier.offset(200.dp, 125.dp))
            Text("Habitat: $habitat", Modifier.offset(200.dp, 150.dp))
            Text(description, Modifier.offset(0.dp, 250.dp))
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    @Composable
    fun CryArea(creature: Creature) {
        val name = creature.name
        Box (modifier = Modifier
            .offset(0.dp, 125.dp)
            .width(425.dp)
            .heightIn(250.dp)
            .fillMaxSize()
            .background(Green40)) {
            Text(name, modifier = Modifier.offset(0.dp, 50.dp), textAlign = TextAlign.Center)
            MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        }
    }

    @Composable
    fun MapArea(creature: Creature) {
        val name = creature.name
        Box (modifier = Modifier
            .offset(0.dp, 125.dp)
            .width(425.dp)
            .heightIn(250.dp)
            .fillMaxSize()
            .background(Green40)) {
            Text(name, modifier = Modifier.offset(0.dp, 50.dp), textAlign = TextAlign.Center)
            Image(painter = painterResource(R.drawable.map), contentDescription = null,
                modifier = Modifier.offset(0.dp, 100.dp), contentScale = ContentScale.FillWidth)
        }
    }
}