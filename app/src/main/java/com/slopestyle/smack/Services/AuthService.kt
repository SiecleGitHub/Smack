package com.slopestyle.smack.Services

import android.content.Context
import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.slopestyle.smack.Utilities.URL_LOGIN
import com.slopestyle.smack.Utilities.URL_REGISTER
import org.json.JSONException
import org.json.JSONObject

object AuthService {
    var isLoggedIn = false
    var userEmail = ""
    var authToken = ""

    /*
     Fully build out web request using Volley to register a user
     */
    fun registerUser(context: Context, email: String, password: String, complete: (Boolean) -> Unit) {

        val jsonBody = JSONObject()
        jsonBody.put("email", email)
        jsonBody.put("password", password)
        val requestbody = jsonBody.toString()
        println(URL_REGISTER)

        val registerRequest = object : StringRequest(
                Method.POST,
                URL_REGISTER,
                Response.Listener { response ->
                    println(response)
                    complete(true)
                },
                Response.ErrorListener { error ->
                    Log.d("ERROR", "Could not register user $email, $password: $error")
                    complete(false)
                }
        ) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return requestbody.toByteArray()
            }
        }

        Volley.newRequestQueue(context).add(registerRequest)
    }


    fun loginUser(context: Context, email: String, password: String, complete: (Boolean) -> Unit) {
        val jsonBody = JSONObject()
        jsonBody.put("email", email)
        jsonBody.put("password", password)
        val requestbody = jsonBody.toString()
        println(URL_LOGIN)

        val loginRequest = object : JsonObjectRequest(
                Method.POST,
                URL_LOGIN,
                null,
                Response.Listener {response ->
                    // this is where we parse the json object
                    try {
                        userEmail = response.getString("user")
                        authToken = response.getString("token")
                        isLoggedIn = true
                        complete(true)
                    } catch (e: JSONException) {
                        Log.d("JSON", "EXC" + e.localizedMessage)
                        complete(false)
                    }
                } ,
                Response.ErrorListener { error ->
                    // this is where we deal with our error
                    Log.d("ERROR", "Could not login user: $email, $password: $error")
                    complete(false)
                }
        ) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return requestbody.toByteArray()
            }
        }

        Volley.newRequestQueue(context).add(loginRequest)
    }


}