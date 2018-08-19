package com.slopestyle.smack.Services

import android.content.Context
import android.content.Intent
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.slopestyle.smack.Controller.App
import com.slopestyle.smack.Utilities.*
import org.json.JSONException
import org.json.JSONObject

object AuthService {
    //var isLoggedIn = false
    //var userEmail = ""
    //var authToken = ""

    /*
     Fully build out web request using Volley to register a user
     */
    fun registerUser(email: String, password: String, complete: (Boolean) -> Unit) {

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

        App.prefs.requestQueue.add(registerRequest)
    }


    fun loginUser(email: String, password: String, complete: (Boolean) -> Unit) {
        val jsonBody = JSONObject()
        jsonBody.put("email", email)
        jsonBody.put("password", password)
        val requestbody = jsonBody.toString()
        println(URL_LOGIN)

        val loginRequest = object : JsonObjectRequest(
                Method.POST,
                URL_LOGIN,
                null,
                Response.Listener { response ->
                    // this is where we parse the json object
                    try {
                        App.prefs.userEmail = response.getString("user")
                        App.prefs.authToken = response.getString("token")
                        App.prefs.isLoggedIn = true
                        complete(true)
                    } catch (e: JSONException) {
                        Log.d("JSON", "EXC" + e.localizedMessage)
                        complete(false)
                    }
                },
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

        App.prefs.requestQueue.add(loginRequest)
    }

    fun createUser(
            name: String,
            email: String,
            avatarName: String,
            avatarColor: String,
            complete: (Boolean) -> Unit
    ) {
        val jsonBody = JSONObject()
        jsonBody.put("name", name)
        jsonBody.put("email", email)
        jsonBody.put("avatarName", avatarName)
        jsonBody.put("avatarColor", avatarColor)
        val requestbody = jsonBody.toString()
        println(URL_CREATE_USER)

        val createRequest = object : JsonObjectRequest(
                Method.POST,
                URL_CREATE_USER,
                null,
                Response.Listener { response ->
                    try {
                        UserDataService.name = response.getString("name")
                        UserDataService.email = response.getString("email")
                        UserDataService.avatarName = response.getString("avatarName")
                        UserDataService.avatarColor = response.getString("avatarColor")
                        UserDataService.id = response.getString("_id")
                        complete(true)
                    } catch (e: JSONException) {
                        Log.d("JSON", "EXC " + e.localizedMessage)
                        complete(false)
                    }
                },
                Response.ErrorListener { error ->
                    Log.d("ERROR", "Could not add user: $email, $name: $error")
                    complete(false)
                }
        ) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return requestbody.toByteArray()
            }

            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers.put("Authorization", "Bearer ${App.prefs.authToken}")
                return headers
            }
        }

        App.prefs.requestQueue.add(createRequest)
    }

    fun findUserByEmail(context: Context, complete: (Boolean) -> Unit) {
        val findUserReguest = object : JsonObjectRequest(
                Method.GET,
                "$URL_GET_USER${App.prefs.userEmail}",
                null,
                Response.Listener { response ->
                    try {
                        UserDataService.name = response.getString("name")
                        UserDataService.email = response.getString("email")
                        UserDataService.avatarName = response.getString("avatarName")
                        UserDataService.avatarColor = response.getString("avatarColor")
                        UserDataService.id = response.getString("_id")
                        val userDataChanged = Intent(USER_DATA_CHANGED_BROADCAST)
                        LocalBroadcastManager.getInstance(context).sendBroadcast(userDataChanged)
                        complete(true)
                    } catch (e: JSONException) {
                        Log.d("JSON", "EXC: " + e.localizedMessage)
                    }
                },
                Response.ErrorListener { error ->
                    Log.d("ERROR", "Could not get user: ${App.prefs.userEmail} : $error")
                }
        ) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers.put("Authorization", "Bearer ${App.prefs.authToken}")
                return headers
            }
        }

        App.prefs.requestQueue.add(findUserReguest)
    }
}
