package com.pv.trackme.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pv.trackme.data.db.AppDatabase
import com.pv.trackme.model.Session
import com.pv.trackme.util.Coroutines

class HistoryViewModel(
    private val database: AppDatabase
) : ViewModel() {
    private val sessions: MutableLiveData<List<Session>> by lazy {
        MutableLiveData<List<Session>>()
    }

    fun loadSessions(rowIndex: Int, numberOfItems: Int) {
        Coroutines.main {
            val sessionDAO = database.getSessionDAO()
            val sessions = sessionDAO.getSessions(rowIndex, numberOfItems).map {
                Session(
                    it.mapImageUrl,
                    it.distance,
                    it.speed,
                    it.time
                )
            }
            this.sessions.value = sessions
        }
    }

    fun getSessions(): LiveData<List<Session>> = sessions

}