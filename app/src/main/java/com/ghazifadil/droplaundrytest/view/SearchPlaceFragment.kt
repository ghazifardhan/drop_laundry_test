package com.ghazifadil.droplaundrytest.view

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ghazifadil.droplaundrytest.R
import com.ghazifadil.droplaundrytest.adapter.PlacesAdapter
import com.ghazifadil.droplaundrytest.viewModel.*
import kotlinx.android.synthetic.main.fragment_search_place.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class SearchPlaceFragment : Fragment() {

    private val permissionViewModel: PermissionViewModel by sharedViewModel()
    private val placesViewModel: PlacesViewModel by sharedViewModel()
    private val placeDetailViewModel: PlaceDetailViewModel by sharedViewModel()
    private val mapsViewModel: MapsViewModel by sharedViewModel()
    private val activityViewModel: ActivityViewModel by sharedViewModel()

    private lateinit var placesAdapter: PlacesAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_place, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        placesViewModel.placeId.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                placeDetailViewModel.getPlaceDetail(it.toString())
            }
        })

        placeDetailViewModel.placeDetail.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                mapsViewModel.setLatLng(it.result.geometry.location.lat, it.result.geometry.location.lng)
                activityViewModel.onChange(search = false)
                Navigation.findNavController(context as Activity, R.id.nav_host_fragment).navigate(R.id.action_search_place_fragment_to_maps_fragment)
            }
        })

        placesViewModel.places.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                val recyclerView: RecyclerView = view.findViewById(R.id.rv_places)
                placesAdapter = PlacesAdapter(context, it.predictions, placesViewModel)
                recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                recyclerView.adapter = placesAdapter
            }
        })

        edit_text_search.setOnEditorActionListener { textView, i, keyEvent ->
            when(i) {
                EditorInfo.IME_ACTION_SEARCH -> {
//                    Log.i("Predictions", textView.text.toString())
                    placesViewModel.getPlaces(textView.text.toString())
                    true
                }
                else -> false
            }
        }
    }
}