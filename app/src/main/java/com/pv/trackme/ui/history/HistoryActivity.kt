package com.pv.trackme.ui.history

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Handler
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.pv.trackme.R
import com.pv.trackme.adapter.HistoryAdapter
import com.pv.trackme.constant.CommonConstant
import com.pv.trackme.model.Session
import com.pv.trackme.ui.base.BaseActivity
import com.pv.trackme.ui.record.RecordActivity
import com.pv.trackme.util.PermissionUtil
import kotlinx.android.synthetic.main.activity_history.*
import org.kodein.di.generic.instance

class HistoryActivity : BaseActivity() {
    companion object {
        private const val REQUEST_CODE_RECORD = 1
    }

    override fun getLayoutId(): Int = R.layout.activity_history

    private val factory: HistoryViewModelFactory by instance()

    private val viewModel: HistoryViewModel by viewModels { factory }

    override fun initViews() {
        if (!isTaskRoot) {
            // Android launched another instance of the root activity into an existing task
            //  so just quietly finish and go away, dropping the user back into the activity
            //  at the top of the stack (ie: the last state of this task)
            finish();
            return;
        }
        rvHistory.apply {
            setAdapter(HistoryAdapter(mutableListOf()))
            setLoadMoreListener { rowIndex, numberOfItems ->
                loadMoreSession(rowIndex, numberOfItems)
            }
        }
        btnRecord.setOnClickListener {
            PermissionUtil.requestPermission(this, getString(R.string.msg_permission_denied), {
                startActivityForResult(RecordActivity.getStartIntent(this), REQUEST_CODE_RECORD)
            }, {

            }, Manifest.permission.ACCESS_FINE_LOCATION)
        }

        viewModel.getSessions().observe(this, Observer<List<Session>> { sessions ->
            rvHistory.addData(sessions)
        })

        loadMoreSession(0)
    }


    private fun loadMoreSession(
        rowIndex: Int,
        numberOfItems: Int = CommonConstant.LOAD_MORE_ITEM_COUNT_DEFAULT
    ) {
        viewModel.loadSessions(rowIndex, numberOfItems)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_RECORD && resultCode == Activity.RESULT_OK) {
            rvHistory.reset()
            loadMoreSession(0)
        }
    }
}