package com.nguyen.topcodertaas_kt

import com.google.gson.annotations.SerializedName

data class Countries(@SerializedName("data") val data: Data) {

    data class Data(@SerializedName("paginationMeta") val pagination: Pagination,
                    @SerializedName("last_update") val lastUpdate: String,
                    @SerializedName("rows") val countries: List<Country>)

    data class Pagination(@SerializedName("currentPage") val currentPage: Int = 0,
                          @SerializedName("totalPages") val totalPages: Int = 0) {
    }
}
