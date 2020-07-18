package com.nguyen.topcodertaas_kt

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

class MapViewModel() : ViewModel() {

    companion object {
        val TAG = "CovidMapViewModel"
    }

    val data = CovidRepository.getAllCountries()

    fun getAllCountries() : LiveData<List<Country>> {
        Log.d("TRUONG", "CovidMapViewModel.getAllCountries")
        return data
    }
}
