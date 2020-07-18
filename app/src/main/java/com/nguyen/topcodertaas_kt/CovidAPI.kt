package com.nguyen.topcodertaas_kt

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CovidAPI {
    @GET("general-stats")
    fun getWorld() : Call<World>

    @GET("countries-search")
    fun getCountries(@Query("order") order: String, @Query("page") page: Int) : Call<Countries>
}
