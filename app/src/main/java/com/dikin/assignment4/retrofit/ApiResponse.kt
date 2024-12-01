package com.dikin.assignment4.retrofit

sealed class ApiResponse<out T> {
    data class Success<out T>(val data: T) : ApiResponse<T>()
    data class Error(val message: String, val throwable: Throwable? = null) : ApiResponse<Nothing>()
    data object Loading : ApiResponse<Nothing>()
}