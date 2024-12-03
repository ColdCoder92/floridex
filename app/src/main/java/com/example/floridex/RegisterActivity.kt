package com.example.floridex

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import com.example.floridex.databinding.RegisterPageBinding

class RegisterActivity : ComponentActivity(), View.OnFocusChangeListener, View.OnKeyListener {

    private lateinit var mBinding: RegisterPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = RegisterPageBinding.inflate(LayoutInflater.from(this))
        setContentView(mBinding.root)
        mBinding.usernameInput.onFocusChangeListener = this
        mBinding.emailInput.onFocusChangeListener = this
        mBinding.passwordInput.onFocusChangeListener = this
        mBinding.confirmPasswordInput.onFocusChangeListener = this

        val loginButton: Button = findViewById(R.id.login_redirect)
        loginButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        val registerButton: Button = findViewById(R.id.register_btn)
        registerButton.setOnClickListener{
            val intent = Intent(this, SettingsActivity::class.java)
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
                        if(validateEmail()){
                            //do validation for uniqueness

                        }
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
                            mBinding.confirmPasswordTIL.setStartIconDrawable(R.drawable.checkmark_circle)
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
                            mBinding.confirmPasswordTIL.setStartIconDrawable(R.drawable.checkmark_circle)
                        }
                    }
                }
            }
        }
    }

    override fun onKey(view: View?, event: Int, keyEvent: KeyEvent?): Boolean {
        return false
    }


}

