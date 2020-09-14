package com.pv.trackme.ui.record

import android.view.View
import com.pv.trackme.R
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