package com.ghazifadil.droplaundrytest.repository

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.ghazifadil.droplaundrytest.R
import com.ghazifadil.droplaundrytest.api.GoogleMapsApi
import com.ghazifadil.droplaundrytest.model.detail_position.DetailPosition
import com.ghazifadil.droplaundrytest.utils.AppResult
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

interface DetailPositionRepository {
    // Suspend is used to await the result from Deferred
    suspend fun getDetailPosition(latLng: LatLng): AppResult<DetailPosition>
}

class DetailPositionRepositoryImpl (
    private val api: GoogleMapsApi,
    private val context: Context
): DetailPositionRepository {

    override suspend fun getDetailPosition(latLng: LatLng): AppResult<DetailPosition> {
        return try {
            val latLngString = "${latLng.latitude},${latLng.longitude}"
            Log.i("asdasd", "$latLngString")
            val mapsKey = context.getString(R.string.google_maps_key)
            val response = api.getDetailPosition(latLngString, "id", "id", mapsKey)

            AppResult.Success(response.body()!!)
        } catch (e: Exception) {
            AppResult.Error(e)
        }
    }

}