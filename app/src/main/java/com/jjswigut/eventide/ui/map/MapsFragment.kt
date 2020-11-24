package com.jjswigut.eventide.ui.map

import android.Manifest
import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.jjswigut.eventide.R
import com.jjswigut.eventide.data.entities.TidalStation
import com.jjswigut.eventide.ui.BaseFragment
import com.jjswigut.eventide.ui.search.SearchFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapsFragment : BaseFragment() {

    private val REQUEST_LOCATION_PERMISSION = 1
    private lateinit var map: GoogleMap
    private val viewModel: SearchFragmentViewModel by activityViewModels()
    private var stationList = arrayListOf<LatLng>()


    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap
        val zoom = 10f
        val loc = viewModel.userLocation.value
        if (loc != null) {
            val location = LatLng(loc.latitude, loc.longitude)
            googleMap.addMarker(MarkerOptions().position(location).title("You are here!"))
            stationList.forEach { station ->
                googleMap.addMarker(MarkerOptions().position(station))
            }

            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, zoom))
            enableMyLocation()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_maps, container, false)
    }


    override fun onResume() {
        super.onResume()
        viewModel.stationLiveData.observe(this) { list ->
            if (!list.isNullOrEmpty()) {
                observeStationsforMarkers(list)
                updateMap()
            }
        }


    }


    private fun isPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun enableMyLocation() {
        if (isPermissionGranted()) {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {

                return
            }
            map.isMyLocationEnabled = true
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )

        }
    }

    private fun observeStationsforMarkers(list: List<TidalStation>) {
        list.forEach { station -> stationList.add(LatLng(station.lat, station.lon)) }

    }

    private fun updateMap() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
}