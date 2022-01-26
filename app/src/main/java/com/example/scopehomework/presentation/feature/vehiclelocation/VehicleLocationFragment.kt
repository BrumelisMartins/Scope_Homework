package com.example.scopehomework.presentation.feature.vehiclelocation

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import com.mapbox.geojson.Point
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.example.scopehomework.R
import com.example.scopehomework.databinding.FragmentVehicleLocationBinding
import com.example.scopehomework.domain.feature.vehiclelocation.entity.Location
import com.example.scopehomework.presentation.State
import com.example.scopehomework.utils.AppPermission
import com.example.scopehomework.utils.isGranted
import com.example.scopehomework.utils.requestAllPermissions
import com.example.scopehomework.utils.requestPermission
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.maps.Style.MAPBOX_STREETS
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import com.mapbox.geojson.LineString
import com.mapbox.core.constants.Constants.PRECISION_6
import com.mapbox.api.directions.v5.models.DirectionsRoute
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.geometry.LatLngBounds
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.layers.LineLayer
import com.mapbox.mapboxsdk.style.layers.Property
import com.mapbox.mapboxsdk.style.layers.PropertyFactory.*


@AndroidEntryPoint
class VehicleLocationFragment : Fragment() {

    private lateinit var binding: FragmentVehicleLocationBinding
    private lateinit var mAdapter: VehicleInfoRecyclerViewAdapter
    private val viewModel: VehicleLocationViewModel by viewModels()
    private val args: VehicleLocationFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (!isGranted(AppPermission.ACCESS_FINE_LOCATION)) {
            requestPermission(AppPermission.ACCESS_FINE_LOCATION)
        }
        (activity as AppCompatActivity).requestAllPermissions(AppPermission.ACCESS_FINE_LOCATION)

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_vehicle_location, container, false)
        mAdapter = VehicleInfoRecyclerViewAdapter()
        binding.vehicleInfoList.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
        binding.mapView.onCreate(savedInstanceState)
        getUserList()
        return binding.root
    }

    private fun getUserList() {
        lifecycleScope.launch {
            viewModel.getVehicleLocation(args.user.userid!!)
                .collect {
                    when (it) {
                        is State.DataState -> setUserDataAndLocation(it.data.data)
                        is State.ErrorState -> {
                            context?.let { context ->
                                showErrorDialog(context)
                            }
                        }
                        is State.LoadingState -> {
                            //Add behaviour if needed while API call is loading
                        }
                    }
                }
        }
    }

    @SuppressLint("MissingPermission")
    private fun showCurrentLocation(mapboxMap: MapboxMap, destination: Point) {
        viewModel.getLocation()?.let {
            val origin = Point.fromLngLat(it.longitude, it.latitude)

            val icon = viewModel.getIcon(resources.getColor(R.color.mapbox_blue))
            mapboxMap.addMarker(
                MarkerOptions()
                    .position(LatLng(origin.latitude(), origin.longitude()))
                    .icon(icon)
            )
            getRoute(origin, destination)
        } ?: showCantShowRouteDialog()


    }

    private fun showCantShowRouteDialog() {
        Toast.makeText(
            context,
            "Sorry, no route available",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun getRoute(origin: Point, destination: Point) {
        lifecycleScope.launch {
            viewModel.getRoute(origin, destination)
                .collect {
                    when (it) {
                        is State.DataState -> drawNavigationPolylineRoute(it.data)
                        is State.ErrorState -> {
                            showCantShowRouteDialog()
                        }
                        is State.LoadingState -> {
                            //Add behaviour if needed while API call is loading
                        }
                    }
                }
        }
    }

    private fun showErrorDialog(context: Context) {
        val dialog = MaterialDialog(context)
            .title(R.string.dialog_title)
            .message(R.string.dialog_message)
        dialog.show {
            positiveButton(R.string.dialog_positive_button) {
                getUserList()
            }
            negativeButton(R.string.dialog_negative_button)
        }
    }

    private fun setUserDataAndLocation(list: List<Location>) {

        lifecycleScope.launch {
            val vehicleDataList = viewModel.getVehicleData(list, args.user.vehicles!!)
            mAdapter.data = vehicleDataList

            binding.mapView.getMapAsync { map ->
                //sets markers and moves camera
                map.setStyle(MAPBOX_STREETS)
                val latLngBounds = LatLngBounds.Builder()
                vehicleDataList.forEach {
                    val icon = viewModel.getIcon(Color.parseColor(it.color))
                    latLngBounds.include(it.point)
                    map.addMarker(
                        MarkerOptions()
                            .position(it.point)
                            .icon(icon)
                    )

                }
                if (vehicleDataList.size > 1) map.animateCamera(
                    CameraUpdateFactory.newLatLngBounds(
                        latLngBounds.build(),
                        viewModel.padding
                    )
                )
                else map.animateCamera(CameraUpdateFactory.newLatLng(vehicleDataList.first().point))

                map.setOnMarkerClickListener { marker ->
                    val destination =
                        Point.fromLngLat(marker.position.longitude, marker.position.latitude)
                    showCurrentLocation(map, destination)
                    true
                }
            }
        }
    }

    private fun drawNavigationPolylineRoute(route: DirectionsRoute) {
        binding.mapView.getMapAsync { map ->
            map.getStyle { style ->
                initLineLayer(style)
                route.geometry()?.let {
                    //center camera between points
                    val routeLine = LineString.fromPolyline(it, PRECISION_6)
                    val cameraPosition = map.getCameraForGeometry(
                        routeLine,
                        intArrayOf(
                            viewModel.padding,
                            viewModel.padding,
                            viewModel.padding,
                            viewModel.padding
                        )
                    )
                    map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition!!))
                }
                style.getSourceAs<GeoJsonSource?>(viewModel.sourceID)
                    ?.setGeoJson(route.geometry()?.let { LineString.fromPolyline(it, PRECISION_6) })
                    ?: showCantShowRouteDialog()
            }
        }
    }

    private fun initLineLayer(loadedMapStyle: Style) {
        val sourceExists = loadedMapStyle.getSource(viewModel.sourceID) != null
        val layerExists = loadedMapStyle.getLayer(viewModel.directionsLayerID) != null

        if (!sourceExists) loadedMapStyle.addSource(GeoJsonSource(viewModel.sourceID))
        if (!layerExists) loadedMapStyle.addLayerBelow(
            LineLayer(
                viewModel.directionsLayerID, viewModel.sourceID
            ).withProperties(
                lineWidth(4.5f),
                lineColor(Color.GREEN),
                lineJoin(Property.LINE_JOIN_ROUND),
                lineCap(Property.LINE_CAP_ROUND)
            ), viewModel.layerBelowID
        )
    }

}


