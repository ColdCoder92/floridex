package com.example.floridex

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.floridex.databinding.LoginPageBinding

class LoginActivity : AppCompatActivity(), View.OnClickListener, View.OnFocusChangeListener,
    View.OnKeyListener {

    private lateinit var mBinding: LoginPageBinding

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
        TODO("Not yet implemented")
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