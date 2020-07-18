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

    lateinit var mapViewModel: MapViewModel
    var map: GoogleMap? = null
    var snapShotFile: File? = null
    val adapter = CountriesAdapter()

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

        val mapFragment = getChildFragmentManager().findFragmentById(R.id.google_map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_item_share -> {
                snapShotFile?.let {
                    Utils.shareImage(this@MapFragment, it)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        map = googleMap
        Log.d("TRUONG", "CovidMapFragment.onMapReady")

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

    fun realMap() {
        mapViewModel.getAllCountries().observe(viewLifecycleOwner, Observer {
            Log.d("TRUONG", "CovidMapFragment.onMapReady.getAllCountries.observe.onChanged")
            // load all country lat-long coordinates from CSV file
            val coordinates = Coordinates(requireContext(), "countries.csv")
            var firstLatLng: LatLng? = null
            adapter.update(it)
            // iterate thru adapter countries
            for (i in 0 until adapter.countries.size) {
                // find lat-long based on country name abbreviation
                val latLng = coordinates.toLatLng(adapter.countries.get(i).countryAbbreviation)
                if (latLng != null) {
                    // make a marker from lat-long found
                    val marker = map!!.addMarker(MarkerOptions().position(latLng).title(adapter.countries.get(i).country))
                    // record country index in marker
                    marker.tag = i
                    // save the first lat-lng if necessary
                    if (firstLatLng == null) {
                        firstLatLng = latLng
                    }
                }
            }
            map?.let {
                // move the camera to the first country in the list, which is USA
                it.moveCamera(CameraUpdateFactory.newLatLng(firstLatLng))
                it.setOnMarkerClickListener {
                    // retrieve from marker index of country selected
                    val index = it?.tag as Int
                    // show bottom sheet of country selected
                    BottomSheetFragment.show(requireContext(), adapter.countries.get(index))
                    false
                }

                // take a snapshot of the google map and save it into file
                it.snapshot {
                    snapShotFile = Utils.saveBitmap(context, it)
                }
            }
        })
    }
}
