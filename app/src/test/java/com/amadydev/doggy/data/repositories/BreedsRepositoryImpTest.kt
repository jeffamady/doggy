package com.amadydev.doggy.data.repositories

import com.amadydev.doggy.data.datasources.BreedsDataSource
import com.amadydev.doggy.data.datasources.Resource
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class BreedsRepositoryImpTest {

    @RelaxedMockK
    private lateinit var dataSource: BreedsDataSource

    private lateinit var repository: BreedsRepositoryImp

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        repository = BreedsRepositoryImp(dataSource)
    }

    @Test
    fun `getAllBreeds when the call is failed return a resId`() = runBlocking {
        val resId = 123
        // Given
        coEvery { dataSource.getAllBreeds() } returns Resource.error(resId)

        // When
        val response = repository.getAllBreeds().resId

        // Then
        coVerify(exactly = 1) { dataSource.getAllBreeds() }
        assert(resId == response)
    }

    @Test
    fun `getAllBreeds when the call is successful return a list`() = runBlocking {
        val list = listOf("akita", "bulldog")
        // Given
        coEvery { dataSource.getAllBreeds() } returns Resource.success(list)

        // When
        val response = repository.getAllBreeds().data

        // Then
        coVerify(exactly = 1) { dataSource.getAllBreeds() }
        assert(list == response)
    }

    @Test
    fun `getBreedImage when the call is failed return a resId`() = runBlocking {
        val resId = 123
        val breed = "akita"
        // Given
        coEvery { dataSource.getBreedImage(breed) } returns Resource.error(resId)

        // When
        val response = repository.getBreedImage(breed).resId

        // Then
        coVerify(exactly = 1) { dataSource.getBreedImage(breed) }
        assert(resId == response)
    }

    @Test
    fun `getBreedImage when the call is successful return an image url`() = runBlocking {
        val breed = "akita"
        val breedImage = "https://amadydev.com/image"
        // Given
        coEvery { dataSource.getBreedImage(breed) } returns Resource.success(breedImage)

        // When
        val response = repository.getBreedImage(breed).data

        // Then
        coVerify(exactly = 1) { dataSource.getBreedImage(breed) }
        assert(breedImage == response)
    }

    @Test
    fun `getAllSubBreeds when the call is failed return a resId`() = runBlocking {
        val breed = "akita"
        val resId = 123
        // Given
        coEvery { dataSource.getAllSubBreeds(breed) } returns Resource.error(resId)

        // When
        val response = repository.getAllSubBreeds(breed).resId

        // Then
        coVerify(exactly = 1) { dataSource.getAllSubBreeds(breed) }
        assert(resId == response)
    }

    @Test
    fun `getAllSubBreeds when the call is successful return a list`() = runBlocking {
        val breed = "akita"
        val list = listOf("akita", "bulldog")
        // Given
        coEvery { dataSource.getAllSubBreeds(breed) } returns Resource.success(list)

        // When
        val response = repository.getAllSubBreeds(breed).data

        // Then
        coVerify(exactly = 1) { dataSource.getAllSubBreeds(breed) }
        assert(list == response)
    }

    @Test
    fun `getSubBreedImage when the call is failed return a resId`() = runBlocking {
        val resId = 123
        val breed = "akita"
        // Given
        coEvery { dataSource.getSubBreedImage(breed, breed) } returns Resource.error(resId)

        // When
        val response = repository.getSubBreedImage(breed, breed).resId

        // Then
        coVerify(exactly = 1) { dataSource.getSubBreedImage(breed, breed) }
        assert(resId == response)
    }

    @Test
    fun `getSubBreedImage when the call is successful return an image url`() = runBlocking {
        val breed = "akita"
        val subBreedImage = "https://amadydev.com/image"
        // Given
        coEvery { dataSource.getSubBreedImage(breed, breed) } returns Resource.success(subBreedImage)

        // When
        val response = repository.getSubBreedImage(breed, breed).data

        // Then
        coVerify(exactly = 1) { dataSource.getSubBreedImage(breed, breed) }
        assert(subBreedImage == response)
    }

    @Test
    fun `searchBreed when the call is failed return a resId`() = runBlocking {
        val resId = 123
        val breed = "akita"
        // Given
        coEvery { dataSource.searchBreed(breed) } returns Resource.error(resId)

        // When
        val response = repository.searchBreed(breed).resId

        // Then
        coVerify(exactly = 1) { dataSource.searchBreed(breed) }
        assert(resId == response)
    }

    @Test
    fun `searchBreed when the call is successful return a list`() = runBlocking {
        val breed = "akita"
        val list = listOf("akita", "bulldog")
        // Given
        coEvery { dataSource.searchBreed(breed) } returns Resource.success(list)

        // When
        val response = repository.searchBreed(breed).data

        // Then
        coVerify(exactly = 1) { dataSource.searchBreed(breed) }
        assert(list == response)
    }
}