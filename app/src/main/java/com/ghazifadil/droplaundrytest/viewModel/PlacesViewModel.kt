package com.ghazifadil.droplaundrytest.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ghazifadil.droplaundrytest.model.places.Places
import com.ghazifadil.droplaundrytest.repository.PlacesRepository
import com.ghazifadil.droplaundrytest.utils.AppResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlacesViewModel(private val placesRepository: PlacesRepository): ViewModel() {

    val state = MutableLiveData<PlacesState>(PlacesState.UNLOAD)
    val places = MutableLiveData<Places>()
    val placeId = MutableLiveData<String>("")

    fun onClickPlace(pId: String) {
        placeId.value = pId
    }

    fun getPlaces(input: String) {
        // set loading
        state.value =  PlacesState.LOADING

        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                placesRepository.getPlaces(input)
            }

            when (result) {
                is AppResult.Success -> {
                    state.value =  PlacesState.LOADED
                    places.value = result.data
                }
                is AppResult.Error -> {
                    Log.i("asdasd", result.exception.localizedMessage)
                    state.value =  PlacesState.ERROR
                }
            }
        }
    }

}

enum class PlacesState {
    UNLOAD, LOADED, LOADING, ERROR
}