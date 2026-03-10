package com.example.carcollection.data.service

import retrofit2.HttpException

sealed class SafeResult<out T> {
    data class Success<out T>(val data: T) : SafeResult<T>()
    data class Error(val code: Int, val message: String) : SafeResult<Nothing>()
}

suspend fun <T> safeApiCall(apiCall: suspend () -> T): SafeResult<T> {
    return try {
        val response = apiCall()
        SafeResult.Success(response)
    } catch (e: Exception) {
        when(e) {
            is HttpException -> {
                val code = e.code()
                val message = e.message()
                SafeResult.Error(code, message)
            }
            else -> {
                SafeResult.Error(-1, e.message ?: "Unknown error")
            }
        }
    }
}
