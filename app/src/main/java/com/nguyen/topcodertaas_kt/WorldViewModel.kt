package com.nguyen.topcodertaas_kt

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

class WorldViewModel() : ViewModel() {
    companion object {
        val TAG = "TotalStatsViewModel"
    }

    var data = CovidRepository.getWorld()

    fun getWorld() : LiveData<World> {
        return data
    }
}
