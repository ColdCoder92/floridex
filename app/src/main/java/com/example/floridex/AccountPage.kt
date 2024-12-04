package com.example.floridex

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.widget.AppCompatButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.google.gson.Gson
import org.json.JSONObject



class AccountPage : ComponentActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.account_page)

        // Initialize views
        usernameTextView = findViewById(R.id.textView)
        emailTextView = findViewById(R.id.textView1)
        accountSettingsButton = findViewById(R.id.accSettings_button)

        // Dummy email for testing
        val email = "test@gmail.com"
        emailTextView.text = email

        // Fetch username from Lambda
        setContent{ GetUsername(
            email,
            context = TODO(),
            dexID = TODO()
        )}


        // Button click event
        val Settings_button: Button = findViewById(R.id.accSettings_button)
        Settings_button.setOnClickListener{
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }

    private lateinit var usernameTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var accountSettingsButton: AppCompatButton

    private lateinit var requestQueue: RequestQueue
    private lateinit var textView: TextView

    // API Gateway endpoint
    private val gatewayLINK =
        "https://umyqtg5cmaomydvm7bfjcyldjq0gmgle.lambda-url.us-east-1.on.aws/"


    @Composable
    fun GetUsername(email: String, context: Context, dexID: Int) {
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
            val rowValue = responseInfo.value.get("username")
            val username: List<User> = gson.fromJson(
                rowValue.toString(), Array<User>::class.java
            ).toList()

            println("Response: ${username[0].username}")

        }
    }



}
