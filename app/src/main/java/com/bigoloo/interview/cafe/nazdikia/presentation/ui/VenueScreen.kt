package com.bigoloo.interview.cafe.nazdikia.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bigoloo.interview.cafe.nazdikia.databinding.HomeScreenBinding
import com.bigoloo.interview.cafe.nazdikia.models.ListViewItem
import com.bigoloo.interview.cafe.nazdikia.models.Venue
import com.bigoloo.interview.cafe.nazdikia.presentation.ui.components.EndlessScrollListener
import com.bigoloo.interview.cafe.nazdikia.presentation.ui.components.VenueAdapter
import com.bigoloo.interview.cafe.nazdikia.presentation.viewmodel.VenueViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class VenueScreen : Fragment() {

    private val venueViewModel: VenueViewModel by viewModel()
    private var binding: HomeScreenBinding? = null
    private fun view(): HomeScreenBinding = binding!!
    private val adapter: VenueAdapter by lazy {
        VenueAdapter(requireContext())
    }
    private val endlessScrollListener = EndlessScrollListener(5) {
        venueViewModel.loadMore()

        //addMoreLoadToRecyclerView()
    }

    private fun addMoreLoadToRecyclerView() {
        venueViewModel.currentState.value!!.venueList.takeIf { it.isNotEmpty() }?.let {
            adapter.setVenueList(
                it.map { ListViewItem.Data(it) }.toMutableList<ListViewItem>().apply {
                    add(ListViewItem.Loading)
                })
            adapter.notifyItemChanged(it.lastIndex)
        }
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
        venueViewModel.currentState.observe(viewLifecycleOwner, Observer { data ->
            if (data.error == null)
                fillData(data.venueList)
            else
                showError(data)
            endlessScrollListener.canLoadMore = data.isLoadMoreNeeded


            //binding.test.text = (pagination as Paginated.Loaded).data.size.toString()
        })
/*
        view().goTONextPage.setOnClickListener {
            findNavController().navigate(HomeScreenDirections.actionHomeScreenToDetailScreen())
        }*/

    }

    private fun showError(data: VenueViewModel.ViewState) {

    }

    private fun fillData(data: List<Venue>) {
        adapter.setVenueList(data.map { ListViewItem.Data(it) })
        adapter.notifyDataSetChanged()
    }

    private fun initView() {

        view().homeVenueList.adapter = adapter
        view().homeVenueList.addOnScrollListener(endlessScrollListener)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}