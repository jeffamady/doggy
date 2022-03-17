package com.amadydev.doggy.data.repositories

import com.amadydev.doggy.data.datasources.Resource

interface BreedsRepository {

    suspend fun getAllBreeds(): Resource<List<String>>

    suspend fun getBreedImage(breed: String): Resource<String>

    suspend fun getAllSubBreeds(breed: String): Resource<List<String>>

    suspend fun getSubBreedImage(breed: String, subBreed: String): Resource<String>

    suspend fun searchBreed(breed: String): Resource<List<String>>
}