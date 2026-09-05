package com.project.prayerreminder.core

import java.io.IOException
import org.json.JSONObject
import retrofit2.HttpException
import timber.log.Timber
import java.net.SocketTimeoutException
import java.net.UnknownHostException

object ErrorMapper {
    fun map(throwable: Throwable?): String {
        return when (throwable) {
            is HttpException -> {

                val code = throwable.code()
                val errorBody = throwable.response()?.errorBody()?.string()

                val apiMessage = parseApiErrorMessage(errorBody)

                apiMessage ?: when (code) {
                    // 4xx client error
                    400 -> "Bad request. Please check your input."
                    403 -> "Forbidden. You don't have permission to access this resource."
                    404 -> "Prayer schedule data not found."
                    408 -> "Request timeout. Please try again."
                    429 -> "Too many requests. Please wait a moment."

                    // 5xx server error
                    500 -> "Internal server error. Please try again later."
                    502 -> "Bad gateway. The server received an invalid response."
                    503 -> "Service unavailable. Aladhan server might be down or under maintenance."
                    504 -> "Gateway timeout. The server took too long to respond."

                    // fallback for other HTTP error
                    else -> "Server error occurred (Code: $code)."
                }
            }

            is SocketTimeoutException -> {
                "Connection timed out. Please check your internet connection."
            }

            is UnknownHostException, is IOException -> {
                "No internet connection. Please check your network settings."
            }

            else -> {
                throwable?.localizedMessage ?: "An unknown error occurred."
            }
        }
    }

    private fun parseApiErrorMessage(errorBody: String?): String? {
        if (errorBody.isNullOrBlank()) return null
        return try {
            val jsonObject = JSONObject(errorBody)
            //Check common keys used by APIs error message
            if (jsonObject.has("data")) {
                jsonObject.getString("data")
            } else if (jsonObject.has("status")) {
                jsonObject.getString("status")
            } else if (jsonObject.has("message")) {
                jsonObject.getString("message")
            } else {
                null
            }
        } catch (e: Exception) {
            Timber.e(e, "Error parsing error body: $errorBody")
            null
        }
    }
}