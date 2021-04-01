package com.ghazifadil.droplaundrytest.common

import com.ghazifadil.droplaundrytest.viewModel.*
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { CountViewModel() }
    viewModel { ActivityViewModel() }
    viewModel { PermissionViewModel() }
    viewModel { MapsViewModel() }
    viewModel { DetailPositionViewModel(detailPositionRepository = get()) }
    viewModel { PlacesViewModel(placesRepository = get()) }
    viewModel { PlaceDetailViewModel(placeDetailRepository = get()) }
}