package com.ghazifadil.droplaundrytest.repository

import android.content.Context
import android.util.Log
import com.ghazifadil.droplaundrytest.R
import com.ghazifadil.droplaundrytest.api.GoogleMapsApi
import com.ghazifadil.droplaundrytest.model.detail_position.DetailPosition
import com.ghazifadil.droplaundrytest.model.place_detail.PlaceDetail
import com.ghazifadil.droplaundrytest.model.places.Places
import com.ghazifadil.droplaundrytest.utils.AppResult
import com.google.android.gms.maps.model.LatLng
import java.lang.Exception

interface PlaceDetailRepository {
    suspend fun getPlaceDetail(placeId: String): AppResult<PlaceDetail>
}

class PlaceDetailRepositoryImpl (
    private val api: GoogleMapsApi,
    private val context: Context
): PlaceDetailRepository {

    override suspend fun getPlaceDetail(placeId: String): AppResult<PlaceDetail> {
        return try {
            val mapsKey = context.getString(R.string.google_maps_key)
            val response = api.getPlaceDetail(placeId, "id", "id", "address_component,name,geometry,formatted_address", mapsKey)

            Log.i("Predictions", response.body().toString())

            AppResult.Success(response.body()!!)
        } catch (e: Exception) {
            AppResult.Error(e)
        }
    }

}