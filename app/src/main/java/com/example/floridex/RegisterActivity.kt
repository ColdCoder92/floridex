package com.example.floridex

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
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
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.floridex.databinding.RegisterPageBinding
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject

class RegisterActivity : ComponentActivity(), View.OnFocusChangeListener{

    private lateinit var mBinding: RegisterPageBinding
    private lateinit var requestQueue: RequestQueue
    private lateinit var textView: TextView
    private val gatewayLINK = "https://z41sqpegib.execute-api.us-east-1.amazonaws.com/userList"
    private var users = ArrayList<User>().toList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = RegisterPageBinding.inflate(LayoutInflater.from(this))
        setContentView(mBinding.root)
        mBinding.composeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent { // In here, we can call composable!
                MaterialTheme {
                    GetUserList()
                }
            }
        }

        val loginButton: Button = findViewById(R.id.login_redirect)
        loginButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        val registerButton: Button = findViewById(R.id.register_btn)
        registerButton.setOnClickListener{
            val usernameInputted = mBinding.usernameInput.text.toString()
            val emailInputted = mBinding.emailInput.text.toString()
            val passwordInputted = mBinding.passwordInput.text.toString()
            var newAccount = true
            for(user in users) {
                if (user.username == usernameInputted) {
                    Toast.makeText(
                        applicationContext,
                        "Username already exists",
                        Toast.LENGTH_SHORT
                    ).show()
                    newAccount = false
                    break
                }
            }
            if(newAccount && validateEmail() && validatePassword() && validateConfirmPassword()
                && validatePasswordMatch()) {
                val intent = Intent(this, CreatureListActivity::class.java)
                startActivity(intent)
                Toast.makeText(applicationContext, "Registration successful", Toast.LENGTH_SHORT).show()
                // Add new user
                val newUser = User(usernameInputted, emailInputted, passwordInputted)
                users = users + newUser

                // Convert updated user list to JSON
                val gson = Gson()
                val updatedUsersJson = gson.toJson(users)

                // Send the updated JSON to the server
                val jsonObjectRequest = JsonObjectRequest(
                    Request.Method.POST,
                    "$gatewayLINK?key1=value1&key2=value2&key3=value3",
                    JSONObject().apply { put("users", JSONArray(updatedUsersJson)) },
                    { _ ->
                        Toast.makeText(applicationContext, "User list updated successfully", Toast.LENGTH_SHORT).show()
                    },
                    { error ->
                        Toast.makeText(applicationContext, "Failed to update user list: ${error.message}", Toast.LENGTH_SHORT).show()
                    }
                )

                // Add the request to the request queue
                requestQueue.add(jsonObjectRequest)
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

    private fun validateEmail(): Boolean {
        var errorMessage: String? = null
        val value: String = mBinding.emailInput.text.toString()
        if (value.isEmpty()) {
            errorMessage = "Email cannot be empty"
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
            errorMessage = "Invalid email"
        }

        if (errorMessage != null) {
            mBinding.emailTIL.apply {
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

    private fun validateConfirmPassword(): Boolean {
        var errorMessage: String? = null
        val value: String = mBinding.passwordInput.text.toString()
        if (value.isEmpty()) {
            errorMessage = "Password cannot be empty"
        } else if (value.length < 6) {
            errorMessage = "Password must be longer than 5 characters"
        }

        if (errorMessage != null) {
            mBinding.confirmPasswordTIL.apply {
                isErrorEnabled = true
                error = errorMessage
            }
        }

        return errorMessage == null
    }

    private fun validatePasswordMatch(): Boolean {
        var errorMessage: String? = null
        val password: String = mBinding.passwordInput.text.toString()
        val confirmPassword: String = mBinding.confirmPasswordInput.text.toString()
        if (password != confirmPassword) {
            errorMessage = "Passwords do not match"
        }

        if (errorMessage != null) {
            mBinding.confirmPasswordTIL.apply {
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

                R.id.email_input -> {
                    if (hasFocus) {
                        if (mBinding.emailTIL.isErrorEnabled) {
                            mBinding.emailTIL.isErrorEnabled = false
                        }
                    } else {
                        validateEmail()
                    }
                }

                R.id.password_input -> {
                    if (hasFocus) {
                        if (mBinding.passwordTIL.isErrorEnabled) {
                            mBinding.passwordTIL.isErrorEnabled = false
                        }
                    } else {
                        if(validatePassword() && mBinding.confirmPasswordInput.text!!.isNotEmpty()
                            && validateConfirmPassword() && validatePasswordMatch()) {
                            if(mBinding.confirmPasswordTIL.isErrorEnabled) {
                                mBinding.confirmPasswordTIL.isErrorEnabled = false
                            }
                        }
                    }
                }

                R.id.confirm_password_input -> {
                    if (hasFocus) {
                        if (mBinding.confirmPasswordTIL.isErrorEnabled) {
                            mBinding.confirmPasswordTIL.isErrorEnabled = false
                        }
                    } else {
                        if(validateConfirmPassword() && validatePassword() && validatePasswordMatch()){
                            if (mBinding.passwordTIL.isErrorEnabled) {
                                mBinding.passwordTIL.isErrorEnabled = false
                            }
                        }
                    }
                }
            }
        }
    }
}

