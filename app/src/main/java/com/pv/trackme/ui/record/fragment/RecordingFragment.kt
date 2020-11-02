package com.pv.trackme.ui.record.fragment

import android.view.View
import com.pv.trackme.R
import com.pv.trackme.ui.base.BaseFragment
import com.pv.trackme.ui.record.RecordAction
import kotlinx.android.synthetic.main.fragment_recording.view.*

class RecordingFragment : BaseFragment() {

    override fun getLayoutId(): Int = R.layout.fragment_recording

    override fun initViews(view: View) {
        view.btnPause.setOnClickListener {
            if (activity is RecordAction?) {
                (activity as RecordAction?)?.onRecordPause()
            }
        }
    }

}