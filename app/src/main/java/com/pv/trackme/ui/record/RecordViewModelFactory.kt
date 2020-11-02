package com.pv.trackme.ui.record

import android.os.Handler
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pv.trackme.data.repository.SessionRepository

class RecordViewModelFactory(
    private val sessionRepository: SessionRepository,
    private val handler: Handler
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return RecordViewModel(sessionRepository, handler) as T
    }

}