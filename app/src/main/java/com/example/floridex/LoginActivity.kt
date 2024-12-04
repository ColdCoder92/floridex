package com.example.floridex

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.ViewCompositionStrategy
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
import com.example.floridex.databinding.LoginPageBinding
import com.google.gson.Gson
import org.json.JSONObject

class LoginActivity : AppCompatActivity(), View.OnFocusChangeListener{

    private lateinit var mBinding: LoginPageBinding
    private lateinit var requestQueue: RequestQueue
    private lateinit var textView: TextView
    private val gatewayLINK = "https://z41sqpegib.execute-api.us-east-1.amazonaws.com/userList"
    private var users = ArrayList<User>().toList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = LoginPageBinding.inflate(LayoutInflater.from(this))
        setContentView(mBinding.root)
        mBinding.composeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent { // In here, we can call composable!
                MaterialTheme {
                    GetUserList()
                }
            }
        }

        val registerButton: Button = findViewById(R.id.register_redirect)
        registerButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)

        }

        val loginButton: Button = findViewById(R.id.login_button)
        loginButton.setOnClickListener{
            val usernameInputted = mBinding.usernameInput.text.toString()
            val passwordInputted = mBinding.passwordInput.text.toString()
            var loginSuccessful = false
            for(user in users) {
                if (user.username == usernameInputted && user.password == passwordInputted) {
                    val intent = Intent(this, DescriptionActivity::class.java)
                    startActivity(intent)
                    Toast.makeText(applicationContext, "Login Successful", Toast.LENGTH_SHORT).show()
                    loginSuccessful = true
                    break
                }
            }
            if (!loginSuccessful){
                Toast.makeText(applicationContext, "Incorrect username or password", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @Composable
    fun GetUserList() {
        requestQueue = Volley.newRequestQueue(applicationContext)
        textView = TextView(applicationContext)

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
            val rowValue = responseInfo.value.get("users")

            users = gson.fromJson(
                rowValue.toString(), Array<User>::class.java
            ).toList()
        }
    }

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
}