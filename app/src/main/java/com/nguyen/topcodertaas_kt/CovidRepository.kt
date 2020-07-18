package com.nguyen.topcodertaas_kt

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object CovidRepository {
    const val TAG = "CovidRepository"
    private const val BASE_URL = "https://corona-virus-stats.herokuapp.com/api/v1/cases/"

    private val covidAPI: CovidAPI
    var world: World? = null
    val countries = mutableListOf<Country>()
    var pagination = Countries.Pagination()

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        covidAPI = retrofit.create(CovidAPI::class.java)
    }

    fun getWorld() : LiveData<World> {
        val data = MutableLiveData<World>()
        if (world != null) {
            data.value = world
        } else {
            covidAPI.getWorld().enqueue(object : Callback<World> {
                override fun onResponse(call: Call<World>, response: Response<World>) {
                    response.body()?.let {
                        world = response.body()
                        data.value = it
                    }
                }

                override fun onFailure(call: Call<World>, t: Throwable) {
                    Log.d(TAG, "getWorld.onFailure")
                }
            })
        }
        return data
    }

    fun getCountries(page: Int) : LiveData<List<Country>> {
        val data = MutableLiveData<List<Country>>()
        if (pagination.totalPages == 0 || (page < pagination.totalPages && page > pagination.currentPage)) {
            // if it's the very first page or some page not beyond the total pages, fetch
            covidAPI.getCountries("total_cases", page).enqueue(object : Callback<Countries> {
                override fun onResponse(call: Call<Countries>, response: Response<Countries>) {
                    response.body()?.let {
                        pagination = it.data.pagination
                        countries.addAll(it.data.countries)
                        Log.d(TAG, "getCountries.onResponse, currentPage: ${pagination.currentPage}")
                        data.value = it.data.countries
                    }
                }

                override fun onFailure(call: Call<Countries>, t: Throwable) {
                    Log.d(TAG, "onFailure: failed to fetch Countries")
                }
            })
        } else if (page < pagination.currentPage) {
            // page has been pre-fetched: return one page worth of data, which is 10 countries
            val from = (page - 1) * 10
            val to = page * 10
            Log.d(TAG, "getCountries, prefetched from $from to $to")
            val tmp = countries.subList(from, to)
            Log.d(TAG, "getCountries, sublist size: ${tmp.count()}")
            data.value = countries.subList(from, to)
        }
        return data
    }

    fun getAllCountries() : LiveData<List<Country>> {
        val data = MutableLiveData<List<Country>>()
        if (pagination.totalPages != 0 && pagination.currentPage >= pagination.totalPages) {
            Log.d(TAG, "getAllCountries, data all fetched")
            data.value = countries
        } else {
            covidAPI.getCountries("total_cases", pagination.currentPage + 1)
                .enqueue(object : Callback<Countries> {
                    override fun onResponse(call: Call<Countries>, response: Response<Countries>) {
                        // if (currentPage < response.body().data.pagination.totalPages) {
                        response.body()?.let {
                            pagination = it.data.pagination
                            Log.d(TAG, "getAllCountries.onResponse, currentPage: ${pagination.currentPage}")
                            countries.addAll(it.data.countries)
                            getAllCountries()
                        }
                    }

                    override fun onFailure(call: Call<Countries>, t: Throwable) {
                        TODO("Not yet implemented")
                    }
                })
        }
        return data
    }
}
