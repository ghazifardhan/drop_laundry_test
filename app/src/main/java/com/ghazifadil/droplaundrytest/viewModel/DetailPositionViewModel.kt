package com.ghazifadil.droplaundrytest.viewModel

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ghazifadil.droplaundrytest.api.GoogleMapsApi
import com.ghazifadil.droplaundrytest.model.detail_position.DetailPosition
import com.ghazifadil.droplaundrytest.repository.DetailPositionRepository
import com.ghazifadil.droplaundrytest.utils.AppResult
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailPositionViewModel(private val detailPositionRepository: DetailPositionRepository): ViewModel() {

    val state = MutableLiveData<DetailPositionState>(DetailPositionState.UNLOAD)
    val detailPosition = MutableLiveData<DetailPosition>()

    fun getDetailPosition(latLng: LatLng) {
        // set loading
        state.value =  DetailPositionState.LOADING

        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                detailPositionRepository.getDetailPosition(latLng)
            }

            when (result) {
                is AppResult.Success -> {
                    state.value =  DetailPositionState.LOADED
                    detailPosition.value = result.data
                }
                is AppResult.Error -> {
                    Log.i("asdasd", result.exception.localizedMessage)
                    state.value =  DetailPositionState.ERROR
                }
            }
        }
    }

}

enum class DetailPositionState {
    UNLOAD, LOADED, LOADING, ERROR
}