package com.ghazifadil.droplaundrytest.common

import com.ghazifadil.droplaundrytest.api.GoogleMapsApi
import org.koin.dsl.module
import retrofit2.Retrofit

val apiModule = module {

    fun provideGoogleMapsApi(retrofit: Retrofit): GoogleMapsApi {
        return retrofit.create(GoogleMapsApi::class.java)
    }
    single { provideGoogleMapsApi(get()) }

}