package com.pv.trackme.ui.record

import android.os.Handler
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pv.trackme.data.db.AppDatabase
import com.pv.trackme.domain.LocationHelper

class RecordViewModelFactory(
    private val database: AppDatabase,
    private val handler: Handler,
    private val locationHelper: LocationHelper
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return RecordViewModel(database, handler, locationHelper) as T
    }

}