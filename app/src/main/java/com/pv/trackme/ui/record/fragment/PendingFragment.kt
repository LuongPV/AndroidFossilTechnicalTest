package com.pv.trackme.ui.record.fragment

import android.view.View
import com.pv.trackme.R
import com.pv.trackme.ui.base.BaseFragment
import com.pv.trackme.ui.record.RecordAction
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