package com.ghazifadil.droplaundrytest

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.navigation.Navigation.findNavController
import com.ghazifadil.droplaundrytest.viewModel.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val activityViewModel: ActivityViewModel by viewModel()
    private val permissionViewModel: PermissionViewModel by viewModel()
    private val mapsViewModel: MapsViewModel by viewModel()
    private val placeViewModel: PlacesViewModel by viewModel()
    private val placeDetailViewModel: PlaceDetailViewModel by viewModel()

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    companion object {
        private const val REQUEST_PERMISSIONS_REQUEST_CODE = 34
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        // remove title
        supportActionBar?.title = ""

        // init fused
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this@MainActivity)

        permissionViewModel.state.observe(this, Observer {
            when (it) {
                PermissionState.CHECK -> {
                    if (!checkPermission()) {
                        permissionViewModel.requestPermission()
                    } else {
                        permissionViewModel.donePermission()
                    }
                }
                PermissionState.REQUEST -> {
                    val shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)

                    if (shouldProvideRationale) {
                        Log.i("shouldProvideRationale", "Displaying permission rationale to provide additional context.")
                        Toast.makeText(this, "shouldProvideRationale true", Toast.LENGTH_LONG).show()
                    } else {
                        Log.i("shouldProvideRationale", "Requesting permission")
                        Toast.makeText(this, "shouldProvideRationale false", Toast.LENGTH_LONG).show()
                        startLocationPermissionRequest()
                    }
                }
                PermissionState.DONE -> {
                    val task = fusedLocationProviderClient.lastLocation
                    task.addOnSuccessListener { location ->
                        if (location != null) {
                            mapsViewModel.setLatLng(location.latitude, location.longitude)
                        }
                    }
                }
                PermissionState.REJECT -> {
                    startLocationPermissionRequest()
                }
                else -> {
                    startLocationPermissionRequest()
                }
            }
        })
    }

    private fun checkPermission(): Boolean {
        val permissionState = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        return permissionState == PackageManager.PERMISSION_GRANTED
    }

    private fun startLocationPermissionRequest() {
        Toast.makeText(this, "startLocationPermissionRequest", Toast.LENGTH_LONG).show()
        ActivityCompat.requestPermissions(this@MainActivity,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_PERMISSIONS_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.contains(PackageManager.PERMISSION_GRANTED)) {
                permissionViewModel.donePermission()
            }
        } else {
            permissionViewModel.requestPermission()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)

        val searchButton: MenuItem = menu.findItem(R.id.nav_search)
        val clearButton: MenuItem = menu.findItem(R.id.nav_clear)
        clearButton.isVisible = false

        activityViewModel.isSearch.observe(this, Observer {
            if (it) {
                clearButton.isVisible = true
                searchButton.isVisible = false
                main_title.text = getString(R.string.search_address)
            } else {
                clearButton.isVisible = false
                searchButton.isVisible = true
                main_title.text = getString(R.string.choose_address)
            }
        })

        searchButton.setOnMenuItemClickListener {
            activityViewModel.onChange(search = true)

            // nullify result
            placeViewModel.onClickPlace("")
            placeDetailViewModel.nullifyPlaceDetail()

            findNavController(this, R.id.nav_host_fragment).navigate(R.id.action_maps_fragment_to_search_place_fragment)
            true
        }

        clearButton.setOnMenuItemClickListener {
            activityViewModel.onChange(search = false)

            findNavController(this, R.id.nav_host_fragment).navigate(R.id.action_search_place_fragment_to_maps_fragment)
            true
        }
        return true
    }
}