package com.ghazifadil.droplaundrytest.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ActivityViewModel: ViewModel() {

    val isSearch = MutableLiveData<Boolean>(false)

    fun onChange(search: Boolean) {
        isSearch.value = search
    }

}
