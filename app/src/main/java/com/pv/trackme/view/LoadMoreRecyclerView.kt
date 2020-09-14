package com.pv.trackme.view

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pv.trackme.adapter.LoadMoreAdapter
import com.pv.trackme.constant.CommonConstant
import com.pv.trackme.util.TwoDataListener
import timber.log.Timber

class LoadMoreRecyclerView(context: Context, attrs: AttributeSet) : RecyclerView(context, attrs) {
    companion object {
        const val VISIBLE_ITEM_BUFFER_LOADING = 5
    }
    private var listener: TwoDataListener<Int, Int>? = null
    private var isLoading = false

    init {
        layoutManager = LinearLayoutManager(context)
        addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val lastVisiblePosition =
                    (recyclerView.layoutManager!! as LinearLayoutManager).findLastVisibleItemPosition()
                val itemCount = recyclerView.adapter!!.itemCount
                if (lastVisiblePosition != itemCount -1 && lastVisiblePosition >= itemCount - VISIBLE_ITEM_BUFFER_LOADING) {
                    if (!isLoading) {
                        isLoading = true
                        listener?.invoke(itemCount, CommonConstant.LOAD_MORE_ITEM_COUNT_DEFAULT)
                    }
                }
            }
        })
    }

    fun setLoadMoreListener(listener: TwoDataListener<Int, Int>) {
        this.listener = listener
    }

    fun <DataType, ViewHolder : RecyclerView.ViewHolder> setAdapter(loadMoreAdapter: LoadMoreAdapter<DataType, ViewHolder>) {
        adapter = loadMoreAdapter
    }

    fun <DataType> addData(dataList: List<DataType>) {
        (adapter as LoadMoreAdapter<DataType, *>).also {
            val itemCount = it.itemCount
            it.addData(dataList)
            it.notifyItemRangeInserted(itemCount - 1, dataList.size)
            isLoading = false
        }
    }

    fun reset() {
        (adapter as LoadMoreAdapter<*, *>).also {
            it.reset()
            it.notifyDataSetChanged()
        }
    }
}