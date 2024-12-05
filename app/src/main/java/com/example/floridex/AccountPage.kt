package com.example.floridex

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.appcompat.widget.AppCompatButton
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.floridex.R.drawable
import com.example.floridex.databinding.AccountPageBinding
import com.google.gson.Gson
import org.json.JSONObject


class AccountPage : ComponentActivity(), View.OnClickListener, View.OnFocusChangeListener, View.OnKeyListener {

    private lateinit var imageView: ImageView
    private lateinit var imageView2: ImageView
    private lateinit var mBinding: AccountPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = AccountPageBinding.inflate(LayoutInflater.from(this))
        setContentView(mBinding.root)


        // Initialize views
        imageView = findViewById(R.id.imageView)
        imageView2 = findViewById(R.id.imageView2)
        usernameTextView = findViewById(R.id.textView)
        emailTextView = findViewById(R.id.textView1)


        //Set images from xml
        // Example: Set images
        imageView.setImageResource(drawable.vector_1)
        imageView2.setImageResource(drawable.group_1)

        // Retrieve email from the intent
        val email = intent.getStringExtra("email")
        emailTextView.text = email // Display the email in the TextView
        Log.d("AccountPage", "Email: $email")


        // Fetch username from Lambda
        if (email != null) {
            fetchUsername(email)
        }


        // Button click event
        val Settings_button: Button = findViewById(R.id.accSettings_button)
        Settings_button.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
            val CreatureList_button: Button = findViewById(R.id.creatureList_button)
            CreatureList_button.setOnClickListener {
                val intent = Intent(this, CreatureListActivity::class.java)
                startActivity(intent)

            }

    }

    private lateinit var usernameTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var accountSettingsButton: AppCompatButton
    private lateinit var creatureListButton: AppCompatButton

    // API Gateway endpoint
    private val gatewayLINK = "https://umyqtg5cmaomydvm7bfjcyldjq0gmgle.lambda-url.us-east-1.on.aws/"


    private fun fetchUsername(email: String) {
        val requestQueue = Volley.newRequestQueue(this)
        val stringRequest = StringRequest(
            Request.Method.GET,
            "$gatewayLINK?email=$email", // Pass email to Lambda
            { response ->
                try {
                    // Parse the outer JSON object to get the "body" field
                    val responseJson = JSONObject(response)
                    val body = responseJson.getString("body")

                    // Now parse the body (which is a JSON string itself) to get the username
                    val bodyJson = JSONObject(body)
                    val username = bodyJson.getString("username")
                    usernameTextView.text = username // Display username
                } catch (e: Exception) {
                    usernameTextView.text = "Error parsing response"
                    Log.e("FetchUsernameError", "Error parsing response", e)
                }
            },
            { error ->
                // Handle errors
                usernameTextView.text = "Error: ${error.message}"
                Log.e("FetchUsernameError", "Volley Error: ${error.message}")
            }
        )
        requestQueue.add(stringRequest)
    }

    override fun onClick(p0: View?) {
        TODO("Not yet implemented")
    }

    override fun onFocusChange(p0: View?, p1: Boolean) {
        TODO("Not yet implemented")
    }

    override fun onKey(p0: View?, p1: Int, p2: KeyEvent?): Boolean {
        TODO("Not yet implemented")
    }


}


