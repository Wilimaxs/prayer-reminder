package com.project.prayerreminder.core

import com.project.prayerreminder.core.data.remote.model.BaseResponse
import retrofit2.HttpException
import retrofit2.Response
import kotlin.coroutines.cancellation.CancellationException

suspend fun <T> safeApiCall(
    call: suspend () -> Response<BaseResponse<T>>
): NetworkResult<T> {
    return try {
        val response = call()

        if (response.isSuccessful) {
            val body = response.body()

            if (body != null && body.code == 200 && body.data != null) {
                NetworkResult.Success(
                    data = body.data,
                    message = body.status ?: "Success"
                )

                // Handle empty body
            } else {
                NetworkResult.Error(
                    message = body?.status ?: "Invalid server response.",
                    code = body?.code ?: response.code()
                )
            }
        } else {
            NetworkResult.Error(
                message = ErrorMapper.map(HttpException(response)),
                code = response.code()
            )
        }
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        NetworkResult.Error(message = ErrorMapper.map(e))
    }
}