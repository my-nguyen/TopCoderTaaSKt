package com.nguyen.topcodertaas_kt

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

class MapViewModel : ViewModel() {

    companion object {
        const val TAG = "CovidMapViewModel"
    }

    val data = CovidRepository.getAllCountries()

    fun getAllCountries() : LiveData<List<Country>> {
        Log.d(TAG, "CovidMapViewModel.getAllCountries")
        return data
    }
}
