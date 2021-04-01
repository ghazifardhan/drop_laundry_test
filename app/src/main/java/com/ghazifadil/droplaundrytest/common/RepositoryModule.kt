package com.ghazifadil.droplaundrytest.common

import android.content.Context
import com.ghazifadil.droplaundrytest.api.GoogleMapsApi
import com.ghazifadil.droplaundrytest.repository.*
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {

    fun provideDetailPositionRepository(api: GoogleMapsApi, context: Context) : DetailPositionRepository {
        return DetailPositionRepositoryImpl(api, context)
    }

    fun providePlacesRepository(api: GoogleMapsApi, context: Context) : PlacesRepository {
        return PlacesRepositoryImpl(api, context)
    }

    fun providePlaceDetailRepository(api: GoogleMapsApi, context: Context) : PlaceDetailRepository {
        return PlaceDetailRepositoryImpl(api, context)
    }

    single { provideDetailPositionRepository(get(), androidContext()) }
    single { providePlacesRepository(get(), androidContext()) }
    single { providePlaceDetailRepository(get(), androidContext()) }
}