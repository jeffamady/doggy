package com.amadydev.doggy.data.repositories

import com.amadydev.doggy.data.datasources.BreedsDataSource
import com.amadydev.doggy.data.datasources.Resource
import javax.inject.Inject

class BreedsRepositoryImp @Inject constructor(
    private val breedsDataSource: BreedsDataSource
) : BreedsRepository {
    override suspend fun getAllBreeds(): Resource<List<String>> =
        breedsDataSource.getAllBreeds()

    override suspend fun getBreedImage(breed: String): Resource<String> =
        breedsDataSource.getBreedImage(breed)

    override suspend fun getAllSubBreeds(breed: String): Resource<List<String>> =
        breedsDataSource.getAllSubBreeds(breed)

    override suspend fun getSubBreedImage(breed: String, subBreed: String): Resource<String> =
        breedsDataSource.getSubBreedImage(breed, subBreed)
}