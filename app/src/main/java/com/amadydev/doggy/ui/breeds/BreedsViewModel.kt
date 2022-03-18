package com.amadydev.doggy.ui.breeds

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amadydev.doggy.data.datasources.Resource
import com.amadydev.doggy.data.models.Dog
import com.amadydev.doggy.data.repositories.BreedsRepositoryImp
import com.amadydev.doggy.utils.Constants
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
    private var _breeds = mutableListOf<String>()
    private val _dogList = mutableListOf<Dog>()
    private val dogList: List<Dog> get() = _dogList
    private var start = Constants.START
    private var end = Constants.END

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
                        reset()
                        data?.let { it ->
                            it.forEach {
                                _breeds.add(it)
                            }
                            loadImages()
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
        _breedsState.value = BreedsState.Loading(true)
        viewModelScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) {
                breedsRepository.getBreedImage(breed)
            }.run {
                when (status) {
                    Resource.Status.SUCCESS -> {
                        // Adding the image to the dogList
                        data?.let {
                            _dogList.add(
                                Dog(breed, it)
                            )
                        }
                        _breedsState.value = BreedsState.Loading(false)
                        _breedsState.value = BreedsState.Success(dogList)
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
                            reset()
                            it.forEach { imageUrl ->
                                _dogList.add(
                                    Dog(query, imageUrl)
                                )
                            }
                            _breedsState.value = BreedsState.Loading(false)
                            _breedsState.value = BreedsState.Success(dogList)
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

    fun loadImages() {
        // To use pagination
        if (end < _breeds.size) {
            _breeds.slice(start..end).forEach { breed ->
                getDogImage(breed)
            }
            start = end
            end += Constants.NEXT
        }
    }

    private fun reset() {
        _breeds.clear()
        _dogList.clear()
        start = Constants.START
        end = Constants.END
    }

    sealed class BreedsState {
        data class Success(val dogsList: List<Dog>) : BreedsState()
        data class Loading(val isLoading: Boolean) : BreedsState()
        data class Error(val resId: Int) : BreedsState()
    }
}