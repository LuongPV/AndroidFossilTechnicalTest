package com.pv.trackme.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pv.trackme.R
import com.pv.trackme.constant.CommonConstant
import com.pv.trackme.model.Session
import com.pv.trackme.util.CommonUtil.isNotAvailable
import com.pv.trackme.util.ImageUtil
import kotlinx.android.synthetic.main.item_session.view.*
import kotlinx.android.synthetic.main.layout_session_summary_info.view.*

class HistoryAdapter(private val sessionList: MutableList<Session>) :
    LoadMoreAdapter<Session, HistoryAdapter.SessionViewHolder>(sessionList) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SessionViewHolder {
        val rootView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_session, parent, false)
        return SessionViewHolder(rootView)
    }

    override fun onBindViewHolder(holder: SessionViewHolder, position: Int) {
        val session = sessionList[position]
        holder.apply {
            val context = rootView.context
            ImageUtil.loadImage(session.mapImageUrl, ivMapImage)
            tvDistance.text = context.getString(
                if (session.distance.isNotAvailable()) R.string.txt_distance_holder else R.string.txt_distance,
                session.distance
            )
            tvSpeed.text = context.getString(
                if (session.speed.isNotAvailable()) R.string.txt_avg_speed_holder else R.string.txt_avg_speed,
                session.speed
            )
            tvTime.text = DateTimeUtil.formatDateTime(
                CommonConstant.TIME_PATTERN_SESSION,
                session.time
            )
        }
    }

    class SessionViewHolder(val rootView: View) : RecyclerView.ViewHolder(rootView) {
        val ivMapImage = rootView.ivMapImage!!
        val tvDistance = rootView.tvDistance!!
        val tvSpeed = rootView.tvSpeed!!
        val tvTime = rootView.tvTime!!
    }
}