package com.pv.trackme.adapter

import androidx.recyclerview.widget.RecyclerView

abstract class LoadMoreAdapter<DataType, ViewHolder : RecyclerView.ViewHolder>(private val dataList: MutableList<DataType>) :
    RecyclerView.Adapter<ViewHolder>() {

    fun getDataList(): List<DataType> = dataList

    fun addData(dataList: List<DataType>) {
        this.dataList.addAll(dataList)
    }

    fun reset() {
        dataList.clear()
    }

    override fun getItemCount() = dataList.size
}