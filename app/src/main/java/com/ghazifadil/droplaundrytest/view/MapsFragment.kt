package com.ghazifadil.droplaundrytest.view

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.ghazifadil.droplaundrytest.R
import com.ghazifadil.droplaundrytest.model.detail_position.DetailPosition
import com.ghazifadil.droplaundrytest.viewModel.*

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_maps.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MapsFragment : Fragment() {

    private val permissionViewModel: PermissionViewModel by sharedViewModel()
    private val mapsViewModel: MapsViewModel by sharedViewModel()
    private val detailPositionViewModel: DetailPositionViewModel by viewModel()

    private lateinit var map: GoogleMap
    private lateinit var currentMarker: Marker

    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap
        val sydney = LatLng(mapsViewModel.latitude.value!!, mapsViewModel.longitude.value!!)

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, mapsViewModel.zoomLevel.value!!))

        permissionViewModel.state.observe(viewLifecycleOwner, Observer {
            if (it == PermissionState.DONE) {
                map.isMyLocationEnabled = true
            }
        })

        mapsViewModel.state.observe(viewLifecycleOwner, Observer {
            if (it == MapsState.LOADED) {
                val pos = LatLng(mapsViewModel.latitude.value!!, mapsViewModel.longitude.value!!)
                currentMarker = googleMap.addMarker(MarkerOptions().position(pos).title("Current Position"))
                currentMarker.tag = 0

                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, mapsViewModel.zoomLevel.value!!))

                detailPositionViewModel.getDetailPosition(pos)
            }
        })

        detailPositionViewModel.detailPosition.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                generateTitleAddress(detailPositionViewModel.detailPosition.value!!)
                generateSubtitleAddress(detailPositionViewModel.detailPosition.value!!)
            }
        })

        // onDrag
        map.setOnCameraMoveListener {
            val newPos = LatLng(map.cameraPosition.target.latitude, map.cameraPosition.target.longitude)
            currentMarker.position = newPos
        }

        // onDragEnd
        map.setOnCameraIdleListener {
            val newPos = LatLng(map.cameraPosition.target.latitude, map.cameraPosition.target.longitude)
            Log.i("setOnCameraIdle", newPos.toString())
            detailPositionViewModel.getDetailPosition(newPos)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    Log.d("TestAjaGan", "Fragment back pressed invoked")
                    // Do custom work here

                    // if you want onBackPressed() to be called as normal afterwards
                    if (isEnabled) {
                        isEnabled = false
                        requireActivity().finish()
                    }
                }
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        choose_address_button.setOnClickListener {
            Toast.makeText(context, "${address_title.text}, ${address_subtitle.text}", Toast.LENGTH_LONG).show()
        }
    }

    private fun generateTitleAddress(detailPosition: DetailPosition) {
        val street = detailPosition.results?.first()?.address_components?.find {
            it.types.contains("route")
        }?.long_name

        address_title.text = street
    }

    private fun generateSubtitleAddress(detailPosition: DetailPosition) {
        val district = detailPosition.results?.first()?.address_components?.find {
            it.types.contains(getString(R.string.district))
        }?.long_name

        val city = detailPosition.results?.first()?.address_components?.find {
            it.types.contains(getString(R.string.city))
        }?.long_name

        val province = detailPosition.results?.first()?.address_components?.find {
            it.types.contains(getString(R.string.province))
        }?.long_name

        val postalCode = detailPosition.results?.first()?.address_components?.find {
            it.types.contains(getString(R.string.postal_code))
        }?.long_name

        address_subtitle.text = getString(R.string.address_subtitle, district, city, province, postalCode)
    }
}