package com.nguyen.topcodertaas_kt

/*class Coordinate(strings: Array<String>) {
    val abbreviation: String
    val latitude: Double
    val longitude: Double

    init {
        abbreviation = strings[0]
        latitude = strings[1].toDouble()
        longitude = strings[2].toDouble()
    }
}*/
data class Coordinate(val abbreviation: String, val latitude: Double, val longitude: Double)