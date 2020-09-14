package com.pv.trackme.ui.record

import android.view.View
import com.pv.trackme.R
import kotlinx.android.synthetic.main.fragment_pending.view.*

class PendingFragment : BaseFragment() {

    override fun getLayoutId(): Int = R.layout.fragment_pending

    override fun initViews(view: View) {
        view.btnResume.setOnClickListener {
            if (activity is RecordAction?) {
                (activity as RecordAction?)?.onRecordResume()
            }
        }
        view.btnStop.setOnClickListener {
            if (activity is RecordAction?) {
                (activity as RecordAction?)?.onRecordStop()
            }
        }
    }

}