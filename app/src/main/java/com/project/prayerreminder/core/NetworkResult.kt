package com.project.prayerreminder.core

sealed class NetworkResult <out T> {
    data class Success<out T>(val data: T, val message: String) : NetworkResult<T>()
    data class Error(val message: String, val code: Int? = null) : NetworkResult<Nothing>()
    object Loading : NetworkResult<Nothing>()
}