package com.amadydev.doggy.data.services

import com.amadydev.doggy.data.models.DogApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface BreedsService {
    @GET("breeds/list")
    suspend fun getAllBreeds () : Response<DogApiResponse<List<String>>>

    @GET("breed/{breed}/images/random")
    suspend fun getBreedImage(
        @Path("breed") breed: String
    ): Response<DogApiResponse<String>>

    @GET("breed/{breed}/list")
    suspend fun getAllSubBreed(
        @Path("breed") breed: String
    ): Response<DogApiResponse<List<String>>>
}