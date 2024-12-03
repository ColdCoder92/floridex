package com.example.floridex

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.floridex.databinding.LoginPageBinding
import com.example.floridex.ui.theme.FloridexTheme
import com.google.gson.Gson

class LoginActivity : AppCompatActivity(), View.OnClickListener, View.OnFocusChangeListener,
    View.OnKeyListener {

    private lateinit var mBinding: LoginPageBinding
    private val showDialog = mutableStateOf(false)


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

            Log.d("myTag","click")
            val usernameInputted = mBinding.usernameInput.text.toString()
            val passwordInputted = mBinding.passwordInput.text.toString()
            if(usernameInputted == "test"){
                Log.d("myTag","test clicked")
            }
            
//            val intent = Intent(this, SettingsActivity::class.java)
//            startActivity(intent)
//            Log.d("myTag",getUserList().toString())


        }
    }



    @Composable
    private fun getUserList(): List<User> {
        val queue = Volley.newRequestQueue(this)
        val url = "https://id5sdg2r34.execute-api.us-east-1.amazonaws.com/filter"
        val userList = mutableListOf<User>()
        val hasItem = remember{mutableStateOf(false)}

        val request = StringRequest(
            Request.Method.GET,
            url, {
                    response ->
                try {
                    val item = Gson().fromJson(response.toString(), User::class.java)
                    userList.add(item)
                    hasItem.value = true
                } catch (e: Exception) {
                    Log.d("Volley",e.message.toString())
                }
            },
            {
                    error ->
                Log.d("Volley",("Error is: $error"))
            }
        )
        queue.add(request)
        if(hasItem.value) {
            return userList
        }
        return emptyList()
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