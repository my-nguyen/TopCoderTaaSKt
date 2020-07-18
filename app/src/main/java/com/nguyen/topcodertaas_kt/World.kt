package com.nguyen.topcodertaas_kt

import com.google.gson.annotations.SerializedName

data class World(@SerializedName("data") val data: Data) {

    data class Data(
        @SerializedName("total_cases") val totalCases: String,
        @SerializedName("recovery_cases") val recoveryCases: String,
        @SerializedName("death_cases") val deathCases: String,
        @SerializedName("last_update") val lastUpdate: String,
        @SerializedName("currently_infected") val currentlyInfected: String
    )
}