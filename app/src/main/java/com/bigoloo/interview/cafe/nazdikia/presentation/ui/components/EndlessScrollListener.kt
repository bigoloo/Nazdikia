package com.bigoloo.interview.cafe.nazdikia.presentation.ui.components

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class EndlessScrollListener(
    private val thresholdVisibleItem: Int = 2,
    private val loadMore: () -> Unit
) : RecyclerView.OnScrollListener() {
    var canLoadMore = false

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        if (!canLoadMore)
            return
        val shouldLoadMore: Boolean =
            (recyclerView.layoutManager as? LinearLayoutManager)?.let { layoutManager ->
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()
                val totalItem = layoutManager.itemCount
                totalItem <= lastVisibleItem + thresholdVisibleItem
            } ?: false

        if (shouldLoadMore) {
            loadMore()
            canLoadMore = false
        }

    }
}