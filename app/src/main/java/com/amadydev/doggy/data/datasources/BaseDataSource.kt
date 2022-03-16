package com.amadydev.doggy.data.datasources

import com.amadydev.doggy.R
import com.amadydev.doggy.data.models.DogApiResponse
import retrofit2.Response
import java.io.Serializable

abstract class BaseDataSource {

    protected suspend fun <T> getResult(call: suspend () -> Response<DogApiResponse<T>>): Resource<T> {
        try {
            val response = call()
            if (response.isSuccessful) {
                if (response.code() == 200) {
                    response.body()?.results?.let {
                        return Resource.success(it)
                    }
                } else {
                    return Resource.error(R.string.no_result)
                }
            }
            return Resource.error(R.string.sorry_something_went_wrong)
        } catch (e: Exception) {
            return Resource.error(R.string.no_internet)
        }
    }
}

data class Resource<out T>(
    val status: Status,
    val data: T?,
    val resId: Int = 0
) : Serializable {

    enum class Status {
        SUCCESS,
        ERROR
    }

    companion object {
        fun <T> success(data: T?): Resource<T> {
            return Resource(
                Status.SUCCESS,
                data
            )
        }

        fun <T> error(resId: Int, data: T? = null): Resource<T> {
            return Resource(
                Status.ERROR,
                data,
                resId
            )
        }
    }
}