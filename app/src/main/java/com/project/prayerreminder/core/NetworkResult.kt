package com.project.prayerreminder.core

sealed class NetworkResult<out T> {
    data class Success<out T>(val data: T, val message: String) : NetworkResult<T>()
    data class Error(val message: String, val code: Int? = null) : NetworkResult<Nothing>()
    object Loading : NetworkResult<Nothing>()
}

inline fun <T> NetworkResult<T>.onSuccess(
    action: (NetworkResult.Success<T>) -> Unit
): NetworkResult<T> {
    if (this is NetworkResult.Success) {
        action(this)
    }
    return this
}

inline fun <T> NetworkResult<T>.onError(
    action: (NetworkResult.Error) -> Unit
): NetworkResult<T> {
    if (this is NetworkResult.Error) {
        action(this)
    }
    return this
}

inline fun <T> NetworkResult<T>.onLoading(
    action: () -> Unit
): NetworkResult<T> {
    if (this === NetworkResult.Loading) {
        action()
    }
    return this
}