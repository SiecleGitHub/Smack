package com.slopestyle.smack.Utilities

const val BASE_URL = "https://chattychatsiecle.herokuapp.com/v1/"
//const val BASE_URL = "http://10.0.2.2:3005/v1/"
const val URL_REGISTER = "${BASE_URL}account/register"
const val URL_LOGIN = "${BASE_URL}account/login"
const val URL_CREATE_USER = "${BASE_URL}user/add"
const val URL_GET_USER = "${BASE_URL}user/byEmail/"

const val USER_DATA_CHANGED_BROADCAST = "USER_DATA_CHANGED_BROADCAST"