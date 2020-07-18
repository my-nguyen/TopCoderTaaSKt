package com.nguyen.topcodertaas_kt

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

class CountriesViewModel : ViewModel() {
    companion object {
        val TAG = "RegionStatsViewModel"
    }

    fun getCountries(page: Int) : LiveData<List<Country>> {
        return CovidRepository.getCountries(page)
    }
}
