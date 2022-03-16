package com.amadydev.doggy.data.models

import com.google.gson.annotations.SerializedName

data class DogApiResponse<T>(
    val status: String,
    @SerializedName("message") val results: T
)
