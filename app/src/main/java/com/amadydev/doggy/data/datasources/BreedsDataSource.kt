package com.amadydev.doggy.data.datasources

import com.amadydev.doggy.data.services.BreedsService
import javax.inject.Inject

class BreedsDataSource @Inject constructor(
    private val breedsService: BreedsService
) : BaseDataSource() {
    suspend fun getAllBreeds() = getResult {
        breedsService.getAllBreeds()
    }

    suspend fun getBreedImage(breed: String) = getResult {
        breedsService.getBreedImage(breed)
    }

    suspend fun getAllSubBreeds(breed: String) = getResult {
        breedsService.getAllSubBreeds(breed)
    }

    suspend fun getSubBreedImage(breed: String, subBreed: String) = getResult {
        breedsService.getSubBreedImage(breed, subBreed)
    }
}