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
}