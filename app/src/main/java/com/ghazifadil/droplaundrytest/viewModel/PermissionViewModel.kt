package com.ghazifadil.droplaundrytest.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PermissionViewModel : ViewModel() {

    val state = MutableLiveData<PermissionState>(PermissionState.CHECK)


    fun checkPermission() {
        state.value = PermissionState.CHECK
    }

    fun requestPermission() {
        state.value = PermissionState.REQUEST
    }

    fun donePermission() {
        state.value = PermissionState.DONE
    }
}

enum class PermissionState {
    REQUEST, DONE, REJECT, CHECK
}