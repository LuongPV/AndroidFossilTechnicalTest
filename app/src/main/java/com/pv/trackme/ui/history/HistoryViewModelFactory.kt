package com.pv.trackme.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pv.trackme.data.repository.SessionRepository

class HistoryViewModelFactory(
    private val sessionRepository: SessionRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HistoryViewModel(sessionRepository) as T
    }

}