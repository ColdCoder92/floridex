package com.example.floridex

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import android.widget.TextView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.volley.AuthFailureError
import com.android.volley.NetworkError
import com.android.volley.NoConnectionError
import com.android.volley.ParseError
import com.android.volley.ServerError
import com.android.volley.TimeoutError
import com.example.floridex.ui.theme.DeepTeal40
import com.example.floridex.ui.theme.DeepTeal80
import com.example.floridex.ui.theme.Green40
import com.example.floridex.ui.theme.Orange40
import com.google.gson.Gson
import org.json.JSONObject

class Login {
    private lateinit var requestQueue: RequestQueue
    private lateinit var textView: TextView
    private val gatewayLINK = "https://z41sqpegib.execute-api.us-east-1.amazonaws.com/userList"
    private var users = ArrayList<User>().toList()
    var rowValue: Any? = null

    @Composable
    fun MakeLoginMenu(context: Context, modifier: Modifier) {
        requestQueue = Volley.newRequestQueue(context)

        val hasResponse = remember { mutableStateOf(false) }
        val responseInfo = remember { mutableStateOf(JSONObject()) }

        val stringRequest = StringRequest(
            Request.Method.GET,
            ("$gatewayLINK?key1=value1&key2=value2&key3=value3"),
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
            rowValue = responseInfo.value.get("users")

            users = gson.fromJson(
                rowValue.toString(), Array<User>::class.java
            ).toList()

            println(rowValue)
        }
//        LoginMenu()
    }

    @Preview
    @Composable
    fun LoginMenu() {

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

        Box (modifier = Modifier.offset(0.dp, 100.dp).width(425.dp).heightIn(50.dp).background(
            Orange40
        ))
        {
            Text("Username", modifier = Modifier.offset(5.dp, 17.5.dp))

            Image(painter = painterResource(R.drawable.edit_icon), contentDescription = null,
                modifier = Modifier.offset(280.dp, 6.5.dp), colorFilter = ColorFilter.tint(Color.Black))

            Text(users[0].username, modifier = Modifier.offset(320.dp, 17.5.dp), textAlign = TextAlign.End)
            // ^ Trying to get this aligned to the right
        }

        Box (modifier = Modifier.offset(0.dp, 151.dp).width(425.dp).heightIn(50.dp).background(
            Orange40
        ))
        {
            Text("Reset Password", modifier = Modifier.offset(5.dp, 17.5.dp))
        }

        Box (modifier = Modifier.offset(0.dp, 202.dp).width(425.dp).heightIn(50.dp).background(
            Orange40
        ))
        {
            Text("Contact Support", modifier = Modifier.offset(5.dp, 17.5.dp))
        }

        Box (modifier = Modifier.offset(0.dp, 253.dp).width(425.dp).heightIn(50.dp).background(Color(0xffff4444)))
        {
            Text("Delete Account", modifier = Modifier.offset(5.dp, 17.5.dp))
            // want this box in a lighter red
        }

        Box (modifier = Modifier.offset(0.dp, 304.dp).width(425.dp).heightIn(50.dp).background(Color(0xffff4444)))
        {
            Text("Sign Out", modifier = Modifier.offset(5.dp, 17.5.dp))
            // want this box in a lighter red
        }

    }
}

