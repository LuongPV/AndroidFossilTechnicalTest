package com.pv.trackme.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pv.trackme.data.db.AppDatabase

class HistoryViewModelFactory(
    private val database: AppDatabase
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HistoryViewModel(database) as T
    }

}