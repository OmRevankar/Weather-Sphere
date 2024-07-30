package com.example.weathersphere.api

import android.os.Message

//used outT to wrap it with any class

//T -> for weather model
sealed class NetworkResponse<out T> {

    data class Success<out T>(val data:T) : NetworkResponse<T>()
    data class Error(val message: String) : NetworkResponse<Nothing>()
    object Loading : NetworkResponse<Nothing>()

}