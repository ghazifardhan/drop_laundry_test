package com.ghazifadil.droplaundrytest.api

import com.ghazifadil.droplaundrytest.model.detail_position.DetailPosition
import com.ghazifadil.droplaundrytest.model.place_detail.PlaceDetail
import com.ghazifadil.droplaundrytest.model.places.Places
import com.google.android.gms.maps.model.LatLng
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleMapsApi {

    @GET("/maps/api/geocode/json")
    suspend fun getDetailPosition(
        @Query("latlng") latlng: String,
        @Query("language") language: String,
        @Query("region") region: String,
        @Query("key") key: String
    ): Response<DetailPosition>

    @GET("/maps/api/place/autocomplete/json")
    suspend fun getPlaces(
        @Query("input") input: String,
        @Query("radius") radius: String,
        @Query("language") language: String,
        @Query("components") components: String,
        @Query("key") key: String
    ): Response<Places>

    @GET("/maps/api/place/details/json")
    suspend fun getPlaceDetail(
        @Query("place_id") placeId: String,
        @Query("language") language: String,
        @Query("region") region: String,
        @Query("fields") fields: String,
        @Query("key") key: String
    ): Response<PlaceDetail>
}