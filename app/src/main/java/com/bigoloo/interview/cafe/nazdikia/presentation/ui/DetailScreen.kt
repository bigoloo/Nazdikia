package com.bigoloo.interview.cafe.nazdikia.presentation.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bigoloo.interview.cafe.nazdikia.R
import com.bigoloo.interview.cafe.nazdikia.databinding.DetailScreenBinding
import com.bigoloo.interview.cafe.nazdikia.models.Location
import com.bigoloo.interview.cafe.nazdikia.presentation.viewmodel.VenueViewModel
import org.koin.android.viewmodel.ext.android.sharedViewModel


class DetailScreen : Fragment() {
    private val venueViewModel: VenueViewModel by sharedViewModel()
    private var binding: DetailScreenBinding? = null
    private fun view(): DetailScreenBinding = binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DetailScreenBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToViewModel()
    }

    private fun subscribeToViewModel() {
        venueViewModel.selectedItemsLiveData.observe(viewLifecycleOwner, Observer { venue ->
            view().detailVenueItemTitle.text = venue.name
            view().detailVenueItemDistance.text = view().detailVenueItemDistance.context.getString(
                R.string.distance_near,
                venue.location.distance
            )
            view().detailVenueItemAddress.text = venue.location.address

            view().detailVenueItemShowInMap.setOnClickListener {
                openMap(venue.location)
            }

        })

    }

    private fun openMap(location: Location) {

        try {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("geo:${location.lat},${location.lng}")
                )
            )
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
            Toast.makeText(requireContext(), getString(R.string.map_cant_open), Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }


}