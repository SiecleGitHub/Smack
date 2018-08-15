package com.slopestyle.smack.Controller

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.view.View
import com.slopestyle.smack.R
import com.slopestyle.smack.Services.AuthService
import com.slopestyle.smack.Utilities.USER_DATA_CHANGED_BROADCAST
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun loginLoginBtnClicked(view: View) {
        val email = loginEmailTxt.text.toString()
        val password = loginPasswordTxt.text.toString()

        AuthService.loginUser(this, email, password) {loginSuccess ->
            if(loginSuccess) {
                AuthService.findUserByEmail(this) {findSuccess ->
                    if(findSuccess) {
                        finish()
                    }
                }
            }

        }

    }

    fun loginCreateUserBtnClicked(view: View) {
        val createUserActivity = Intent(this, CreateUserActivity::class.java)
        startActivity(createUserActivity)
        finish()
    }
}
