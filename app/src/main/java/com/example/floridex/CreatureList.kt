package com.example.floridex

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.widget.TextView
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import com.android.volley.AuthFailureError
import com.android.volley.NetworkError
import com.android.volley.NoConnectionError
import com.android.volley.ParseError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.ServerError
import com.android.volley.TimeoutError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.floridex.ui.theme.DeepTeal40
import com.example.floridex.ui.theme.Green40
import com.example.floridex.ui.theme.Orange40
import com.google.gson.Gson
import org.json.JSONObject

data class Item(
    val dexID: Int,
    val name: String,
    val habitat: String,
    val description: String,
    val weight: Double,
    val height: Double,
    val image: String,
    val type: String,
    val author: String,
    val sound: String
)

class CreatureList: AppCompatActivity() {
    private lateinit var requestQueue: RequestQueue
    private lateinit var textView: TextView
    private val gatewayLink = "https://r37p9ee0yj.execute-api.us-east-1.amazonaws.com/list"
    @Composable
    fun MakeCreatureList(modifier: Modifier, context: Context, onItemSelected: (Int) -> Unit) {
        // If you need to perform some initialization or side effect when the composable is first shown
        LaunchedEffect(Unit) {
            // This block runs once when the composable is first launched.
            // You can perform actions like logging, setting up data, or other side effects here.
        }

        requestQueue = Volley.newRequestQueue(context)
        textView = TextView(context)
        val hasResponse = remember { mutableStateOf(false) }
        var wildlifeResponse = remember { mutableStateOf(JSONObject()) }

        val creatureListRequest = StringRequest(
            Request.Method.GET,
            gatewayLink,
            { response ->
                textView.text = response
                wildlifeResponse.value = JSONObject(response)
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

        requestQueue.add(creatureListRequest)

        if (hasResponse.value) {
            val gson = Gson()
            val rows = wildlifeResponse.value.get("wildlife")
            val items: List<Item> = gson.fromJson(
                rows.toString(), Array<Item>::class.java
            ).toList()

            BackgroundTheme()
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(items.size) { index ->
                    DescriptionItem(item = items[index], onItemSelected = onItemSelected)
                }
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
    fun DescriptionItem(item: Item, onItemSelected: (Int) -> Unit) {
        val context = LocalContext.current
        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult(),
            onResult = { result -> {}}
        )
        val imageData = context.assets.open("images/${item.image}").readBytes()

        // Decode the Image Data into a Bitmap
        val bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.size)
        val imageBitmap = bitmap.asImageBitmap()

        val imageHeight = imageBitmap.height
        val imageWidth = imageBitmap.width

        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .background(Green40)
                .padding(16.dp)
                .clickable {
                    onItemSelected(item.dexID) // Trigger the onItemSelected callback
                    val intent = Intent(context, DescriptionActivity::class.java)
                    intent.putExtra("itemId", item.dexID)
                    launcher.launch(intent)
                }
        ) {
            Text(item.name, textAlign = TextAlign.Center)
            Image(
                bitmap = imageBitmap,
                contentDescription = null,
                modifier = if (imageWidth > imageHeight) {
                    Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                } else {
                    Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .rotate(90f)
                },
                contentScale = ContentScale.Crop
            )
            Text(item.description.substring(0, 100) + "...", textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }


}

@Preview
@Composable
fun PreviewDescription() {
    val list = CreatureList()
    list.MakeCreatureList(modifier = Modifier, context = LocalContext.current, onItemSelected = {})
}