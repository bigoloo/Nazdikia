package com.bigoloo.interview.cafe.nazdikia.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bigoloo.interview.cafe.nazdikia.R
import com.bigoloo.interview.cafe.nazdikia.databinding.HomeScreenBinding
import com.bigoloo.interview.cafe.nazdikia.models.*
import com.bigoloo.interview.cafe.nazdikia.presentation.ui.components.EndlessScrollListener
import com.bigoloo.interview.cafe.nazdikia.presentation.ui.components.VenueAdapter
import com.bigoloo.interview.cafe.nazdikia.presentation.viewmodel.VenueViewModel
import kotlinx.android.synthetic.main.home_screen.view.*
import org.koin.android.viewmodel.ext.android.sharedViewModel

class VenueScreen : Fragment() {

    private val venueViewModel: VenueViewModel by sharedViewModel()
    private var binding: HomeScreenBinding? = null
    private fun view(): HomeScreenBinding = binding!!
    private lateinit var adapter: VenueAdapter
    private val endlessScrollListener = EndlessScrollListener(5) {
        venueViewModel.loadMore()

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = HomeScreenBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        subscribeToViewModel()

    }

    private fun subscribeToViewModel() {
        venueViewModel.state.observe(viewLifecycleOwner, Observer { state ->

            when (state) {
                FirstPageNotLoaded -> {
                    view().pageMessageError.isVisible = false
                    view().firstPageLoading.isVisible = false
                    view().homeVenueList.isVisible = false

                }
                FirstPageLoading -> {
                    view().pageMessageError.isVisible = false
                    view().firstPageLoading.isVisible = true
                    view().homeVenueList.isVisible = false
                }
                is FirstPageFailed -> {
                    view().pageMessageError.apply {
                        isVisible = true
                    }
                    view().firstPageLoading.isVisible = false
                    view().homeVenueList.isVisible = false
                    handleException(state.throwable)
                    showRetryButton()

                }
                is PageLoaded -> {
                    view().firstPageLoading.isVisible = false

                    if (state.result.data.isEmpty()) {
                        view().homeVenueList.isVisible = false
                        view().pageMessageError.apply {
                            isVisible = true
                            text = getString(R.string.data_is_empty)
                        }
                        view().firstPageLoading.isVisible = true
                    } else {
                        view().reloadVenue.isVisible = false
                        view().pageMessageError.isVisible = false
                        view().homeVenueList.isVisible = true
                        fillData(state.result.data)
                        endlessScrollListener.canLoadMore = state.result.isLoadMoreNeeded
                    }
                }
                is PageFailed -> {
                    handleException(state.throwable)
                    endlessScrollListener.canLoadMore = state.result.isLoadMoreNeeded
                }
                is PageLoading -> {
                    addMoreLoadToRecyclerView(state.result.data)
                }
            }
        })
    }

    private fun showRetryButton() {
        view().reloadVenue.apply {
            isVisible = true
            setOnClickListener {
                venueViewModel.loadMore()
                view().reloadVenue.isVisible = false
                view().firstPageLoading.firstPageLoading.isVisible = true
            }
        }
    }

    private fun addMoreLoadToRecyclerView(data: List<Venue>) {

        val newData: List<ListViewItem> =
            data.map { ListViewItem.Data(it) }.toMutableList() + listOf(ListViewItem.Loading)
        adapter.setVenueList(newData)
        adapter.notifyItemChanged(newData.lastIndex)

    }

    private fun handleException(throwable: Throwable) {

        Toast.makeText(
            requireContext(), when (throwable) {
                UnAvailableLocationException -> {
                    R.string.error_location_is_off
                }
                else -> {
                    R.string.error_unknown
                }
            }, Toast.LENGTH_SHORT
        ).show()
    }

    private fun fillData(data: List<Venue>) {

        adapter.setVenueList(data.map { ListViewItem.Data(it) })
        adapter.notifyDataSetChanged()
    }

    private fun initView() {
        adapter = VenueAdapter(requireContext()) {
            venueViewModel.selectedVenueItem(it)
            findNavController().navigate(VenueScreenDirections.actionHomeScreenToDetailScreen())
        }
        view().homeVenueList.layoutManager = LinearLayoutManager(context)

        view().homeVenueList.adapter = adapter
        view().homeVenueList.addOnScrollListener(endlessScrollListener)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}