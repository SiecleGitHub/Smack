package com.slopestyle.smack.Controller

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.slopestyle.smack.R
import com.slopestyle.smack.Services.AuthService
import com.slopestyle.smack.Utilities.USER_DATA_CHANGED_BROADCAST
import kotlinx.android.synthetic.main.activity_create_user.*
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        loginSpinner.visibility = View.INVISIBLE
    }

    fun loginLoginBtnClicked(view: View) {
        enableSpinner(true)
        val email = loginEmailTxt.text.toString()
        val password = loginPasswordTxt.text.toString()
        hideKeyboard()

        if(email.isNotEmpty() && password.isNotEmpty()) {
            AuthService.loginUser(this, email, password) {loginSuccess ->
                if(loginSuccess) {
                    AuthService.findUserByEmail(this) {findSuccess ->
                        if(findSuccess) {
                            enableSpinner(false)
                            finish()
                        } else {
                            toastError()
                        }
                    }
                } else {
                    toastError()
                }
            }
        } else {
            Toast.makeText(this, "Please fill in both email and password", Toast.LENGTH_LONG).show()
        }


    }

    fun loginCreateUserBtnClicked(view: View) {
        val createUserActivity = Intent(this, CreateUserActivity::class.java)
        startActivity(createUserActivity)
        finish()
    }

    fun toastError() {
        Toast.makeText(this, "Something went wrong, please try again.", Toast.LENGTH_LONG).show()
        enableSpinner(false)
    }

    fun enableSpinner(enable: Boolean) {
        if(enable) {
            loginSpinner.visibility = View.VISIBLE
        } else {
            loginSpinner.visibility = View.INVISIBLE
        }

        loginLoginBtn.isEnabled = !enable
        loginCreateUserBtn.isEnabled = !enable
    }

    fun hideKeyboard() {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        if(inputManager.isAcceptingText) {
            inputManager.hideSoftInputFromWindow(currentFocus.windowToken, 0)
        }
    }
}
