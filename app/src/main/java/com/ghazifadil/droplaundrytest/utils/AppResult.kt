package com.ghazifadil.droplaundrytest.utils

sealed class AppResult<out T : Any> {
    class Success<out T : Any>(val data: T) : AppResult<T>()
    class Error(val exception: Throwable) : AppResult<Nothing>()
}