package com.nguyen.topcodertaas_kt

import android.content.Context
import com.google.android.gms.maps.model.LatLng
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser

class Coordinates(context: Context, filename: String) {
    val list = mutableListOf<Coordinate>()

    init {
        // creates a list of Coordinates from a CSV file located in the assets folder
        context.assets.open(filename).bufferedReader().use {
            val parser = CSVParser(it, CSVFormat.DEFAULT)
            for (record in parser) {
                val coordinate = Coordinate(record.get(0), record.get(1).toDouble(), record.get(2).toDouble())
                list.add(coordinate)
            }
        }
    }

    // search in the list of coordinates for the country whose abbreviation matches the parameter
    private fun search(abbreviation: String) : Int {
        var left = 0
        var right = list.size - 1
        while (left <= right) {
            val middle = left + (right - left) / 2
            if (list[middle].abbreviation.compareTo(abbreviation) == 0) {
                return middle
            } else if (list[middle].abbreviation < abbreviation) {
                left = middle + 1
            } else {
                right = middle - 1
            }
        }
        return -1
    }

    // converts a country abbreviation to a latitude-longitude pair
    fun toLatLng(country: String): LatLng? {
        val index = search(country)
        return if (index != -1) {
            val coordinate = list[index]
            LatLng(coordinate.latitude, coordinate.longitude)
        } else {
            null
        }
    }
}
