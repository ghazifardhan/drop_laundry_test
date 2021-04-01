package com.ghazifadil.droplaundrytest.repository

import android.content.Context
import android.util.Log
import com.ghazifadil.droplaundrytest.R
import com.ghazifadil.droplaundrytest.api.GoogleMapsApi
import com.ghazifadil.droplaundrytest.model.detail_position.DetailPosition
import com.ghazifadil.droplaundrytest.model.places.Places
import com.ghazifadil.droplaundrytest.utils.AppResult
import com.google.android.gms.maps.model.LatLng
import java.lang.Exception

interface PlacesRepository {
    suspend fun getPlaces(input: String): AppResult<Places>
}

class PlacesRepositoryImpl(
    private val api: GoogleMapsApi,
    private val context: Context
): PlacesRepository {

    override suspend fun getPlaces(input: String): AppResult<Places> {
        return try {
            val mapsKey = context.getString(R.string.google_maps_key)
            val response = api.getPlaces(input, "1", "id", "country:id", mapsKey)

            Log.i("Predictions", response.body().toString())

            AppResult.Success(response.body()!!)
        } catch (e: Exception) {
            AppResult.Error(e)
        }
    }

}