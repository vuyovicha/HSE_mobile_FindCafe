package com.example.new_bottom_navigation_ui


import com.hse.findcafe.PlaceHolder
import com.hse.findcafe.ui.home.RouteHolder
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query



interface TomTomApiService {

    @GET("https://api.tomtom.com/routing/1/calculateRoute/{lat1},{lng1}:{lat2},{lng2}/json?key=mvGcs6aeG2AtpsnUn1yr4hdNBUB7gVkG&travelMode=bicycle")
    fun route(@Path("lat1") lat1: Double,
               @Path("lng1") lng1: Double,
               @Path("lat2") lat2: Double,
               @Path("lng2") lng2: Double): Call<RouteHolder>

    @GET("https://api.tomtom.com/search/2/search/{request}.json/?key=mvGcs6aeG2AtpsnUn1yr4hdNBUB7gVkG")
    fun find(@Path("request") request: String): Call<PlaceHolder>

    companion object Factory {
        fun create(): TomTomApiService {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://api.tomtom.com/")
                .build()

            return retrofit.create(TomTomApiService::class.java);
        }
    }
}