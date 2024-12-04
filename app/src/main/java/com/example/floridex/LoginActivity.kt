package com.example.floridex

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
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

class LoginActivity : AppCompatActivity(), View.OnClickListener, View.OnFocusChangeListener,
    View.OnKeyListener {

    private lateinit var mBinding: LoginPageBinding
    private lateinit var requestQueue: RequestQueue
    private lateinit var textView: TextView
    private val gatewayLINK = "https://z41sqpegib.execute-api.us-east-1.amazonaws.com/userList"
    var users = ArrayList<User>().toList()
    var tryLogin = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = LoginPageBinding.inflate(LayoutInflater.from(this))
        mBinding.usernameInput.onFocusChangeListener = this
        mBinding.passwordInput.onFocusChangeListener = this
        mBinding.passwordInput.setOnKeyListener(this)
        mBinding.loginButton.setOnClickListener(this)
        mBinding.registerRedirect.setOnClickListener(this)
        setContentView(mBinding.root)
        mBinding.composeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent { // In here, we can call composables!
                MaterialTheme {
                    Greeting(name = "compose")
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
            for(user in users) {
                if (user.username == usernameInputted && user.password == passwordInputted) {
                    val intent = Intent(this, SettingsActivity::class.java)
                    startActivity(intent)
                } else{
                    Toast.makeText(applicationContext, "Incorrect username or password", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    @Composable
    fun Greeting(name: String) {
        GetUserList()
        if(tryLogin){
            tryLogin = false
        }
    }

    @Composable
    fun GetUserList() {
        requestQueue = Volley.newRequestQueue(applicationContext)
        textView = TextView(applicationContext)

        val hasResponse = remember { mutableStateOf(false) }
        var responseInfo = remember { mutableStateOf(JSONObject()) }

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

            println("Response: " + rowValue)
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

    override fun onClick(v: View?) {

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