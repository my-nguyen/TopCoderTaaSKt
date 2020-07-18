package com.nguyen.topcodertaas_kt

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.io.File

class MapFragment : Fragment(), OnMapReadyCallback {

    companion object {
        const val TAG = "MapFragment"
    }

    private lateinit var mapViewModel: MapViewModel
    private lateinit var snapShotFile: File
    private var map: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mapViewModel = ViewModelProvider(this).get(MapViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_map, container, false)

        val mapFragment = childFragmentManager.findFragmentById(R.id.google_map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_item_share -> {
                Utils.shareImage(this@MapFragment, snapShotFile)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        map = googleMap
        Log.d(TAG, "CovidMapFragment.onMapReady")

        // testMap()
        realMap()
    }

    fun testMap() {
        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        map?.let {
            it.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
            it.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        }
    }

    private fun realMap() {
        mapViewModel.getAllCountries().observe(viewLifecycleOwner, Observer { allCountries ->
            Log.d(TAG, "CovidMapFragment.onMapReady.getAllCountries.observe.onChanged")
            // load all country lat-long coordinates from CSV file
            val coordinates = Coordinates(requireContext(), "countries.csv")
            var firstLatLng: LatLng? = null
            map?.let { map ->
                // iterate thru adapter countries
                for (i in allCountries.indices) {
                    // find lat-long based on country name abbreviation
                    val latLng = coordinates.toLatLng(allCountries[i].countryAbbreviation)
                    if (latLng != null) {
                        // make a marker from lat-long found
                        val marker = map.addMarker(MarkerOptions().position(latLng).title(allCountries[i].country))
                        // record country index in marker
                        marker.tag = i
                        // save the first lat-lng if necessary
                        if (firstLatLng == null) {
                            firstLatLng = latLng
                        }
                    }
                }
                // move the camera to the first country in the list, which is USA
                map.moveCamera(CameraUpdateFactory.newLatLng(firstLatLng))
                map.setOnMarkerClickListener {
                    // retrieve from marker index of country selected
                    val index = it?.tag as Int
                    // show bottom sheet of country selected
                    BottomSheetFragment.show(requireContext(), allCountries[index])
                    false
                }

                // take a snapshot of the google map and save it into file
                map.snapshot {
                    snapShotFile = Utils.saveBitmap(context, it)
                }
            }
        })
    }
}
