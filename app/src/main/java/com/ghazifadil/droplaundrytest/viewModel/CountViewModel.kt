package com.ghazifadil.droplaundrytest.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CountViewModel: ViewModel() {

    private var _count = 0

    val state = MutableLiveData<State>()
    val count = MutableLiveData<Int>()

    init {
        count.value = 0
    }

    fun increment() {
        state.value = State.INCREMENT
        count.value = count.value?.plus(1)
    }

    fun decrement() {
        state.value = State.DECREMENT
        count.value = count.value?.minus(1)
    }

}

enum class State {
    UNLOAD, INCREMENT, DECREMENT
}