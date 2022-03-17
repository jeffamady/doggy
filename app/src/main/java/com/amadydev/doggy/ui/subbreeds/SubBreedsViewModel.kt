package com.amadydev.doggy.ui.subbreeds

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
class SubBreedsViewModel @Inject constructor(
    private val breedsRepository: BreedsRepositoryImp
) : ViewModel() {
    private val _subBreedsState = MutableLiveData<SubBreedsState>()
    val subBreedsState: LiveData<SubBreedsState> = _subBreedsState
    private val mDogList = mutableListOf<Dog>()

    fun getAllSubBreeds(breed: String) {
        _subBreedsState.value = SubBreedsState.Loading(true)
        viewModelScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) {
                breedsRepository.getAllSubBreeds(breed)
            }.run {
                when (status) {
                    Resource.Status.SUCCESS -> {
                        data?.let { it ->
                            it.ifEmpty {
                                _subBreedsState.value = SubBreedsState.Loading(false)
                                _subBreedsState.value = SubBreedsState.Empty
                            }
                            it.forEach { subBreed ->
                                getSubBreedImage(breed, subBreed, it.size)
                            }
                        }
                    }
                    Resource.Status.ERROR -> {
                        _subBreedsState.value = SubBreedsState.Loading(false)
                        _subBreedsState.value = SubBreedsState.Error(resId)
                    }
                }
            }
        }
    }

    private fun getSubBreedImage(breed: String, subBreed: String, size: Int) {
        viewModelScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) {
                breedsRepository.getSubBreedImage(breed, subBreed)
            }.run {
                when (status) {
                    Resource.Status.SUCCESS -> {
                        // Adding the image to the dogList
                        data?.let {
                            mDogList.add(
                                Dog(subBreed, it)
                            )
                        }
                        if (mDogList.size == size) {
                            _subBreedsState.value = SubBreedsState.Loading(false)
                            _subBreedsState.value = SubBreedsState.Success(mDogList)
                        }
                    }
                    Resource.Status.ERROR -> {
                        _subBreedsState.value = SubBreedsState.Loading(false)
                        _subBreedsState.value = SubBreedsState.Error(resId)
                    }
                }
            }
        }
    }


    sealed class SubBreedsState {
        data class Success(val dogsList: List<Dog>) : SubBreedsState()
        data class Loading(val isLoading: Boolean) : SubBreedsState()
        data class Error(val resId: Int) : SubBreedsState()
        object Empty : SubBreedsState()
    }
}