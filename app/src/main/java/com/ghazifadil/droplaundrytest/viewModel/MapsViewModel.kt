package com.ghazifadil.droplaundrytest.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MapsViewModel: ViewModel() {

    val state = MutableLiveData<MapsState>(MapsState.UNLOAD)
    val latitude = MutableLiveData<Double>(-6.225199551349465)
    val longitude = MutableLiveData<Double>(106.84844981878994)
    val zoomLevel = MutableLiveData<Float>(15f)

    fun setLatLng(lat: Double, lng: Double) {
        latitude.value = lat
        longitude.value = lng
        state.value = MapsState.LOADED
    }

}

enum class MapsState {
    UNLOAD, LOADED
}