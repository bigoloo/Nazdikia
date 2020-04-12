package com.bigoloo.interview.cafe.nazdikia.presentation.ui.components

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bigoloo.interview.cafe.nazdikia.databinding.VenueItemViewBinding
import com.bigoloo.interview.cafe.nazdikia.databinding.VenueLoadingItemViewBinding
import com.bigoloo.interview.cafe.nazdikia.models.ListViewItem
import com.bigoloo.interview.cafe.nazdikia.models.Venue

class VenueAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val itemDataType = 0
    private val loadingItemType = 1

    private var venueList = emptyList<ListViewItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val inflater = LayoutInflater.from(context)
        return if (viewType == 0) {
            VenueItemVIewHolder(VenueItemViewBinding.inflate(inflater))
        } else {
            LoadingItemVIewHolder(VenueLoadingItemViewBinding.inflate(inflater))
        }

    }

    override fun getItemCount(): Int {
        return venueList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is VenueItemVIewHolder) {
            holder.bind((venueList[position] as ListViewItem.Data<Venue>).data)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (venueList[position]) {
            ListViewItem.Loading -> loadingItemType
            is ListViewItem.Data<*> -> itemDataType
        }
    }

    fun setVenueList(venueList: List<ListViewItem>) {
        this.venueList = venueList
    }

    private class VenueItemVIewHolder(private val view: VenueItemViewBinding) :
        RecyclerView.ViewHolder(view.root) {
        fun bind(venue: Venue) {
            view.itemVenueTitle.text = venue.name
        }
    }

    private class LoadingItemVIewHolder(view: VenueLoadingItemViewBinding) :
        RecyclerView.ViewHolder(view.root)
}