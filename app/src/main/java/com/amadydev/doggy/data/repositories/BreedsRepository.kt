package com.amadydev.doggy.data.repositories

import com.amadydev.doggy.data.datasources.Resource

interface BreedsRepository {

    suspend fun getAllBreeds(): Resource<List<String>>

    suspend fun getBreedImage(breed: String): Resource<String>
}