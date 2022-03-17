package com.amadydev.doggy.ui.breeds

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amadydev.doggy.data.datasources.Resource
import com.amadydev.doggy.data.models.Dog
import com.amadydev.doggy.data.repositories.BreedsRepositoryImp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class BreedsViewModel @Inject constructor(
    private val breedsRepository: BreedsRepositoryImp
) : ViewModel() {
    private val _breedsState = MutableLiveData<BreedsState>()
    val breedsState: LiveData<BreedsState> = _breedsState
    private val mDogList = mutableListOf<Dog>()

    init {
        getAllBreeds()
    }

    private fun getAllBreeds() {
        _breedsState.value = BreedsState.Loading(true)
        viewModelScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) {
                breedsRepository.getAllBreeds()
            }.run {
                when (status) {
                    Resource.Status.SUCCESS -> {
                        mDogList.clear()
                        data?.let { it ->
                            it.slice(0..15).forEach { breed ->
//                            it.forEach { breed ->
                                getDogImage(breed)
                            }
                        }
                    }
                    Resource.Status.ERROR -> {
                        _breedsState.value = BreedsState.Loading(false)
                        _breedsState.value = BreedsState.Error(resId)
                    }
                }
            }
        }
    }

    fun retry() = getAllBreeds()

    private fun getDogImage(breed: String) {
        viewModelScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) {
                breedsRepository.getBreedImage(breed)
            }.run {
                when (status) {
                    Resource.Status.SUCCESS -> {
                        // Adding the image to the dogList
                        data?.let {
                            mDogList.add(
                                Dog(breed, it)
                            )
                        }
//                        if (mDogList.size == size) {
                        _breedsState.value = BreedsState.Loading(false)
                        _breedsState.value = BreedsState.Success(mDogList)
//                        }
                    }
                    Resource.Status.ERROR -> {
                        _breedsState.value = BreedsState.Loading(false)
                        _breedsState.value = BreedsState.Error(resId)
                    }
                }
            }
        }
    }

    fun searchBreed(query: String) {
        _breedsState.value = BreedsState.Loading(true)
        viewModelScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) {
                breedsRepository.searchBreed(query)
            }.run {
                when (status) {
                    Resource.Status.SUCCESS -> {
                        data?.let { it ->
//                            it.slice(0..15).forEach { breed ->
                            mDogList.clear()
                            it.forEach { imageUrl ->
                                mDogList.add(
                                    Dog(query, imageUrl)
                                )
                            }
                            _breedsState.value = BreedsState.Loading(false)
                            _breedsState.value = BreedsState.Success(mDogList)
                        }
                    }
                    Resource.Status.ERROR -> {
                        _breedsState.value = BreedsState.Loading(false)
                        _breedsState.value = BreedsState.Error(resId)
                    }
                }
            }
        }
    }


    sealed class BreedsState {
        data class Success(val dogsList: List<Dog>) : BreedsState()
        data class Loading(val isLoading: Boolean) : BreedsState()
        data class Error(val resId: Int) : BreedsState()
    }
}