package com.fulbiopretell.retoyape.ui.fragment

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.fulbiopretell.retoyape.R
import com.fulbiopretell.retoyape.base.viewBinding
import com.fulbiopretell.retoyape.databinding.FragmentMapScreenBinding
import com.fulbiopretell.retoyape.util.AppUtils
import com.fulbiopretell.retoyape.util.LocationController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint

class MapScreenFragment : Fragment(), LocationController.LocationControllerListener, OnMapReadyCallback {

    private var mMap: GoogleMap? = null
    private var queryLocation: Location? = null
    private var locationController: LocationController? = null
    private var latitude: Double? = null
    private var longitude: Double? = null
    private var title: String? = null

    private val binding by lazy {
        FragmentMapScreenBinding.inflate(LayoutInflater.from(activity), null, false)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments.let {
            latitude = it?.getDouble(LOCATION_RECIPE_LATITUDE)
            longitude = it?.getDouble(LOCATION_RECIPE_LONGITUDE)
            title = it?.getString(LOCATION_RECIPE_TITLE)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mapView.onCreate(savedInstanceState)
        try {
            MapsInitializer.initialize(requireContext())
        } catch (e: Exception) {
            e.printStackTrace()
        }
        binding.mapView.getMapAsync(this)
    }

    override fun onLocationPermissionDenied() {
    }

    override fun onGpsUnavailable() {
    }

    @SuppressLint("MissingPermission")
    override fun onGetLocationCompleted(location: Location?) {

        val drawable: Int = R.drawable.ic_marker_sky_blue

        queryLocation = location
        mMap!!.isMyLocationEnabled = true
        val currentLocation = LatLng(location!!.latitude, location.longitude)
        val recipeLocation = LatLng(latitude!!,longitude!!)

        locationController?.disconnect()

        val latLng = LatLng(latitude!!, longitude!!)
        val markerOptions = MarkerOptions()
        markerOptions.position(latLng)
        markerOptions.title(title)
        markerOptions.icon(
            AppUtils.bitmapDescriptorFromVector(
                context,
                drawable
            )
        )

        val marker = mMap!!.addMarker(markerOptions)
        marker?.showInfoWindow()

        val bounds = LatLngBounds.Builder()
            .include(currentLocation)
            .include(recipeLocation).build()
        val cu = CameraUpdateFactory.newLatLngBounds(bounds, 250)
        mMap!!.animateCamera(cu)
    }

    override fun onLocationUpdate(location: Location?) {
    }

    override fun onError(error: LocationController.LocationManagerError?) {
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap?.getUiSettings()?.setZoomControlsEnabled(false)
        mMap?.getUiSettings()?.setMapToolbarEnabled(false)
        mMap?.getUiSettings()?.setMyLocationButtonEnabled(true)
        mMap?.setOnMyLocationButtonClickListener({ false })
        val peru = LatLng(-12.084155, -76.975721)
        mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(peru, 5.0f))
    }

    override fun onResume() {
        super.onResume()
        locationController = LocationController(requireContext(), 1000, this@MapScreenFragment)
        locationController?.connect()
        binding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }
}