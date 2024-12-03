package com.example.floridex

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.NetworkError
import com.android.volley.NoConnectionError
import com.android.volley.ParseError
import com.android.volley.Request
import com.android.volley.ServerError
import com.android.volley.TimeoutError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.floridex.databinding.LoginPageBinding
import org.json.JSONObject

class LoginActivity : AppCompatActivity(), View.OnClickListener, View.OnFocusChangeListener,
    View.OnKeyListener {

    private lateinit var mBinding: LoginPageBinding
    private val gatewayLINK = "https://id5sdg2r34.execute-api.us-east-1.amazonaws.com/filter"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = LoginPageBinding.inflate(LayoutInflater.from(this))
        mBinding.usernameInput.onFocusChangeListener = this
        mBinding.passwordInput.onFocusChangeListener = this
        mBinding.passwordInput.setOnKeyListener(this)
        mBinding.loginButton.setOnClickListener(this)
        mBinding.registerRedirect.setOnClickListener(this)
        setContentView(mBinding.root)

        val registerButton: Button = findViewById(R.id.register_redirect)
        registerButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)

        }

        val loginButton: Button = findViewById(R.id.login_button)
        loginButton.setOnClickListener{
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }

//    private fun openFile() {
//        val filePath = "com/example/floridex/CreatureList.kt"
//        val fileUri = Uri.parse(filePath)
//        val intent = Intent(Intent.ACTION_VIEW).apply {
//            setDataAndType(fileUri, "text/plain") //
//            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//        }
//        startActivity(intent)
//    }

    private fun validateUsername(): Boolean {
        var errorMessage: String? = null
        val value: String = mBinding.usernameInput.text.toString()
        if (value.isEmpty()) {
            errorMessage = "Full name cannot be empty"
        }

        if (errorMessage != null) {
            mBinding.usernameTIL.apply {
                isErrorEnabled = true
                error = errorMessage
            }
        }

        return errorMessage == null
    }

    private fun validatePassword(): Boolean {
        var errorMessage: String? = null
        val value: String = mBinding.passwordInput.text.toString()
        if (value.isEmpty()) {
            errorMessage = "Password cannot be empty"
        } else if (value.length < 6) {
            errorMessage = "Password must be longer than 5 characters"
        }

        if (errorMessage != null) {
            mBinding.passwordTIL.apply {
                isErrorEnabled = true
                error = errorMessage
            }
        }

        return errorMessage == null
    }

    private fun login(username: String, password: String) {

        val requestQueue = Volley.newRequestQueue(this)
        val url = "$gatewayLINK?username=$username&password=$password" // Pass query params if supported by API

        val stringRequest = object : StringRequest(
            Request.Method.GET,
            url,
            { response ->

                try {
                    val jsonResponse = JSONObject(response)
                    val isValid = jsonResponse.getBoolean("isValid") // Adjust based on your API response structure

                    if (isValid) {
                        // Successful login
                        val intent = Intent(this, RegisterActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            },
            { error ->

                // Handle errors
                val errorMessage = when (error) {
                    is TimeoutError, is NoConnectionError -> "Network error. Please check your connection."
                    is AuthFailureError -> "Authentication error."
                    is ServerError -> "Server error. Please try again later."
                    is NetworkError -> "Network error. Please try again."
                    is ParseError -> "Response parsing error."
                    else -> "An unknown error occurred."
                }
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                return headers
            }
        }

        // Add the request to the queue
        requestQueue.add(stringRequest)
    }

    override fun onClick(v: View?) {
//        when (v?.id) {
//            R.id.login_button -> {
//                if (validateUsername() && validatePassword()) {
//                    // Send request to validate login credentials
//                    login(mBinding.usernameInput.text.toString(), mBinding.passwordInput.text.toString())
//                }
//            }
//            R.id.register_redirect -> {
//                val intent = Intent(this, RegisterActivity::class.java)
//                startActivity(intent)
//            }
//        }
    }

    override fun onFocusChange(view: View?, hasFocus: Boolean) {
        if (view != null) {
            when (view.id) {
                R.id.username_input -> {
                    if (hasFocus) {
                        if (mBinding.usernameTIL.isErrorEnabled) {
                            mBinding.usernameTIL.isErrorEnabled = false
                        }
                    } else {
                        validateUsername()
                    }
                }

                R.id.password_input -> {
                    if (hasFocus) {
                        if (mBinding.passwordTIL.isErrorEnabled) {
                            mBinding.passwordTIL.isErrorEnabled = false
                        }
                    } else {
                            validatePassword()
                        }
                    }
                }

            }
        }

    override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
        return false
    }
}