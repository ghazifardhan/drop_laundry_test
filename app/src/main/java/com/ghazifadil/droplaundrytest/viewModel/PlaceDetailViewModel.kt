package com.ghazifadil.droplaundrytest.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ghazifadil.droplaundrytest.model.place_detail.PlaceDetail
import com.ghazifadil.droplaundrytest.repository.PlaceDetailRepository
import com.ghazifadil.droplaundrytest.utils.AppResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlaceDetailViewModel(private val placeDetailRepository: PlaceDetailRepository): ViewModel() {

    val state = MutableLiveData<PlaceDetailState>(PlaceDetailState.UNLOAD)
    val placeDetail = MutableLiveData<PlaceDetail>()

    fun nullifyPlaceDetail() {
        placeDetail.value = null
    }

    fun getPlaceDetail(placeId: String) {
        // set loading
        state.value =  PlaceDetailState.LOADING

        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                placeDetailRepository.getPlaceDetail(placeId)
            }

            when (result) {
                is AppResult.Success -> {
                    state.value =  PlaceDetailState.LOADED
                    placeDetail.value = result.data
                }
                is AppResult.Error -> {
                    Log.i("asdasd", result.exception.localizedMessage)
                    state.value =  PlaceDetailState.ERROR
                }
            }
        }
    }

}

enum class PlaceDetailState {
    UNLOAD, LOADED, LOADING, ERROR
}