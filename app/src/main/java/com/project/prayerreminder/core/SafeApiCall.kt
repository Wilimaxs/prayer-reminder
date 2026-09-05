package com.project.prayerreminder.core

import com.project.prayerreminder.core.data.remote.model.BaseResponse
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
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
            } else {
                NetworkResult.Error(
                    message = body?.status ?: "Unknown server response."
                )
            }
        } else {
            NetworkResult.Error(
                message = ErrorMapper.map(HttpException(response))
            )
        }
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        NetworkResult.Error(
            message = ErrorMapper.map(e)
        )
    }
}