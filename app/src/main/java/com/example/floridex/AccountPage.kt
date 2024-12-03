/*
package com.example.floridex

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AccountPage : AppCompatActivity() {

        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.account_page)

        // Find views
        val usernameTextView: TextView = findViewById(R.id.textView)
        val accountSettingsButton: AppCompatButton = findViewById(R.id.button)

        // Fetch username from the API
        fetchUsername(usernameTextView)

        // Navigate to Settings when the button is clicked
        accountSettingsButton.setOnClickListener {
            val intent = Intent(this, Settings::class.java)
            startActivity(intent)
        }
    }

    /**
     * Fetch the username from the API and set it to the TextView
     */
    private fun fetchUsername(textView: TextView) {
        val call = ApiClient.userApi.getUser()

        call.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    val user = response.body()
                    textView.text = user?.username ?: "No username found"
                } else {
                    textView.text = "Error: ${response.code()}"
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                textView.text = "Failed to load username"
                Log.e("AccountPage", "API Error", t)
            }
        })
    }
}

*/