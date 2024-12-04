package com.example.floridex

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.Picture
import android.media.ImageReader
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
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
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import com.android.volley.AuthFailureError
import com.android.volley.NetworkError
import com.android.volley.NoConnectionError
import com.android.volley.ParseError
import com.android.volley.ServerError
import com.android.volley.TimeoutError
import org.json.JSONObject
import com.google.gson.Gson
//import com.example.floridex.AccountPage


data class Creature(
    val dexID: Int,
    val name: String,
    val habitat: String,
    val description: String,
    val weight: Double,
    val height: Double,
    val image: CreatureImage,
    val type: String,
    val author: String,
    val sound: String
)

data class CreatureImage(
    val type: String,
    val data: ByteArray
)

data class Comment(
    val id: Int,
    val creatureID: Int,
    val username: String,
    val comment: String
)

class Description: AppCompatActivity() {
    private lateinit var requestQueue: RequestQueue
    private lateinit var textView: TextView
    private val gatewayLINK = "https://id5sdg2r34.execute-api.us-east-1.amazonaws.com/filter"
    private val gatewayLINK2 = "https://xgerowymh2.execute-api.us-east-1.amazonaws.com/filter"

    val Context.screenWidth: Int
        get() = resources.displayMetrics.widthPixels

    val Context.screenHeight: Int
        get() = resources.displayMetrics.heightPixels

    @Composable
    fun MakeDescription(modifier: Modifier, context: Context, dexID: Int) {
        requestQueue = Volley.newRequestQueue(context)
        textView = TextView(context)
        val hasWildlifeResponse = remember { mutableStateOf(false) }
        val hasCommentsResponse = remember { mutableStateOf(false) }
        var wildlifeResponse = remember { mutableStateOf(JSONObject()) }
        var commentsResponse = remember { mutableStateOf(JSONObject()) }

        val creatureRequest = StringRequest(
            Request.Method.GET,
            ("$gatewayLINK?id=$dexID"),
            { response ->
                textView.text = response
                wildlifeResponse.value = JSONObject(response)
                hasWildlifeResponse.value = true
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

        val commentsRequest = StringRequest(
            Request.Method.GET,
            ("$gatewayLINK2?id=$dexID"),
            { response ->
                textView.text = response
                commentsResponse.value = JSONObject(response)
                hasCommentsResponse.value = true
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

        requestQueue.add(creatureRequest)
        requestQueue.add(commentsRequest)

        val gson = Gson()

        if (hasWildlifeResponse.value && hasCommentsResponse.value) {
            val rowValue = wildlifeResponse.value.get("wildlife")
            val creatures: List<Creature> = gson.fromJson(
                rowValue.toString(), Array<Creature>::class.java
            ).toList()

            println("Response: ${creatures[0].sound}")
            BackgroundTheme()
            MenuNav(context)
            ProfileNav(creatures[0].author)
            DescriptionArea(creatures[0])
            DescriptionTabButtons(creatures[0], context)
        }

        if (hasCommentsResponse.value) {
            val commentRowValue = commentsResponse.value.get("comments")
            val comments: List<Comment> = gson.fromJson(
                commentRowValue.toString(), Array<Comment>::class.java
            ).toList()

            CommentsSection(comments)
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
    fun ProfileNav(email: String) {
        val profilePressed = remember { mutableStateOf(false) }
        Button(modifier = Modifier.offset(350.dp, 37.5.dp).width(50.dp).height(50.dp),
            onClick = {
                profilePressed.value = true
            }
        ) {}
        Image(painter = painterResource(R.drawable.profile), contentDescription = null,
            modifier = Modifier.offset(350.dp, 37.5.dp).width(50.dp).height(50.dp)
        )
    /*  Profile Page implementation in progress
        if (profilePressed.value) {
            AccountPage().someMethod(., email)
        }
     */
    }

    @Composable
    fun MenuNav(context: Context) {
        val backPressed = remember { mutableStateOf(false) }
        Button(modifier = Modifier.offset(16.dp, 37.5.dp).width(50.dp).height(50.dp),
            onClick = {
                backPressed.value = true
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
        ) {}
        Image(painter = painterResource(R.drawable.back_arrow), contentDescription = null,
            modifier = Modifier.offset(16.dp, 37.5.dp).width(50.dp).height(50.dp),
            colorFilter = ColorFilter.tint(DeepTeal40)
        )
        if (backPressed.value) {
            val intent = Intent(this, CreatureListActivity::class.java)
            context.startActivity(intent)
        }
    }

    @Composable
    fun DescriptionTabButtons(creature: Creature, context: Context) {
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
            DescriptionTabButtons(creature, context)
        }

        if (cryPressed.value) {
            println("Cry page")
            CryArea(creature, context)
            DescriptionTabButtons(creature, context)
        }

        if (mapPressed.value) {
            println("Map page")
            MapArea(creature)
            DescriptionTabButtons(creature, context)
        }
    }

    @Composable
    fun DescriptionArea(creature: Creature) {
        val name = creature.name
        val habitat = creature.habitat
        val weight = creature.weight
        val height = creature.height
        val description = creature.description
        val imageData = creature.image.data

        // Decode the Image Data into a Bitmap
        val bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.size)
        val imageBitmap = bitmap.asImageBitmap()

        Box (modifier = Modifier
            .offset(0.dp, 125.dp)
            .width(425.dp)
            .heightIn(300.dp)
            .fillMaxSize()
            .background(Green40)) {
            Text(name, modifier = Modifier.offset(0.dp, 50.dp), textAlign = TextAlign.Center)
            Image(bitmap = imageBitmap, contentDescription = null,
                modifier = Modifier
                    .offset(0.dp, 100.dp)
                    .width(200.dp)
                    .rotate(90f),
                contentScale = ContentScale.FillWidth
            )
            Text("$weight Kilograms", Modifier.offset(200.dp, 100.dp))
            Text("$height Meters", Modifier.offset(200.dp, 125.dp))
            Text("Habitat: $habitat", Modifier.offset(200.dp, 150.dp))
            Text(description, Modifier.offset(0.dp, 300.dp))
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun CryArea(creature: Creature, context: Context) {
        val name = creature.name

        Box (modifier = Modifier
            .offset(0.dp, 125.dp)
            .width(425.dp)
            .heightIn(250.dp)
            .fillMaxSize()
            .background(Green40)) {
                Column(modifier = Modifier.fillMaxWidth().fillMaxHeight().padding(16.dp)) {
                    if (creature.sound.isNotEmpty()) {
                        val audioLocation = context.assets.openFd("audio/${creature.sound}")
                        val mediaPlayer = MediaPlayer()
                        val playing = remember { mutableStateOf(false) }
                        val position = remember { mutableFloatStateOf(0F) }
                        mediaPlayer.setDataSource(
                            audioLocation.fileDescriptor,
                            audioLocation.startOffset,
                            audioLocation.length
                        )
                        mediaPlayer.setVolume(100F, 100F)
                        mediaPlayer.prepare()
                        Text(
                            name, modifier = Modifier, textAlign = TextAlign.Center
                        )
                        Icon(
                            imageVector = Icons.Default.PlayArrow, contentDescription = null,
                            modifier = Modifier.size(50.dp).clickable(
                                enabled = true,
                                onClick = {
                                    if (!playing.value) {
                                        mediaPlayer.start()
                                    } else {
                                        mediaPlayer.stop()
                                        mediaPlayer.prepareAsync()
                                        mediaPlayer.start()
                                    }
                                    playing.value = true

                                    object : CountDownTimer(mediaPlayer.duration.toLong(), 100) {
                                        override fun onTick(millisUntilFinished: Long) {
                                            position.floatValue =
                                                mediaPlayer.currentPosition.toFloat()
                                        }

                                        override fun onFinish() {
                                            playing.value = false
                                        }
                                    }.start()
                                })
                        )
                    } else {
                        Text("No audio available")
                    }
                }
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

    @Composable
    fun CommentsSection(comments: List<Comment>) {
        Box(modifier = Modifier
            .offset(0.dp, 700.dp)
            .width(425.dp)
            .heightIn(250.dp)
            .fillMaxSize()
            .background(Green80)) {
            Text("Comments", textAlign = TextAlign.Center)
            if (comments.isEmpty()) {
                Text(
                    "No comments yet",
                    modifier = Modifier.offset(y = 50.dp).align(Alignment.Center)
                )
            } else {
                val rowHeight = 25.dp
                var count = 1
                for (comment in comments) {
                    Row(modifier = Modifier.offset(y = 25.dp)) {
                        Text(
                            comment.username,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f).offset(y=rowHeight*count)
                        )
                        Text(comment.comment, modifier = Modifier.weight(3f).offset(y=rowHeight*count))
                    }
                    count++
                }
            }
        }
        for (comment in comments) {
            Text(comment.comment)
        }
    }
}