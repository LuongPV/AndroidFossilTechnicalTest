package com.pv.trackme.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pv.trackme.data.db.AppDatabase
import com.pv.trackme.data.repository.SessionRepository
import com.pv.trackme.model.Session
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val sessionRepository: SessionRepository
) : ViewModel() {
    private val sessions: MutableLiveData<List<Session>> by lazy {
        MutableLiveData<List<Session>>()
    }

    fun loadSessions(rowIndex: Int, numberOfItems: Int) {
        viewModelScope.launch {
            val sessions = sessionRepository.getCalculatedSessions(rowIndex, numberOfItems).map {
                Session(
                    it.mapImageUrl,
                    it.distance,
                    it.speed,
                    it.time
                )
            }
            this@HistoryViewModel.sessions.value = sessions
        }
    }

    fun getSessions(): LiveData<List<Session>> = sessions

}