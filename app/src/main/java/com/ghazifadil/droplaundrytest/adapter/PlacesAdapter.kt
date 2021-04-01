package com.ghazifadil.droplaundrytest.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ghazifadil.droplaundrytest.R
import com.ghazifadil.droplaundrytest.model.places.Predictions
import com.ghazifadil.droplaundrytest.viewModel.MapsViewModel
import com.ghazifadil.droplaundrytest.viewModel.PermissionViewModel
import com.ghazifadil.droplaundrytest.viewModel.PlacesViewModel
import org.koin.androidx.viewmodel.compat.SharedViewModelCompat.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class PlacesAdapter(
    private val context: Context?,
    private val dataSet: List<Predictions>,
    private val placesViewModel: PlacesViewModel
) : RecyclerView.Adapter<PlacesAdapter.PlacesViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlacesAdapter.PlacesViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_places, parent, false)

        return PlacesViewHolder(view)
    }

    class PlacesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val placeTitle: TextView
        val placeSubtitle: TextView
        val placeContainer: LinearLayout

        init {
            placeTitle = view.findViewById(R.id.place_title)
            placeSubtitle = view.findViewById(R.id.place_subtitle)
            placeContainer = view.findViewById(R.id.place_container)
        }
    }

    override fun getItemCount() = dataSet.size

    override fun onBindViewHolder(holder: PlacesViewHolder, position: Int) {
        holder.placeTitle.text = dataSet[position].structured_formatting.main_text
        holder.placeSubtitle.text = dataSet[position].structured_formatting.secondary_text

        holder.placeContainer.setOnClickListener {
            Log.i("PlaceContainer", dataSet[position].structured_formatting.main_text)

            placesViewModel.onClickPlace(dataSet[position].place_id)
        }
    }
}