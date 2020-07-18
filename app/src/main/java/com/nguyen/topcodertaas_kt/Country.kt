package com.nguyen.topcodertaas_kt

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Country(@SerializedName("country") val country: String,
                   @SerializedName("country_abbreviation") val countryAbbreviation: String,
                   @SerializedName("total_cases") val totalCases: String,
                   @SerializedName("total_deaths") val totalDeaths: String,
                   @SerializedName("total_recovered") val totalRecovered: String,
                   @SerializedName("active_cases") val activeCases: String,
                   @SerializedName("flag") val flag: String
) : Serializable
