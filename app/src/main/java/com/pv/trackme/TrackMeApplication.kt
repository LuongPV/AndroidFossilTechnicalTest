package com.pv.trackme

import android.app.Application
import android.os.Handler
import com.pv.trackme.data.db.AppDatabase
import com.pv.trackme.data.preference.AppPreferenceImpl
import com.pv.trackme.domain.FusedLocationUpdateHelper
import com.pv.trackme.domain.LocationHelperImpl
import com.pv.trackme.ui.history.HistoryViewModelFactory
import com.pv.trackme.ui.record.RecordViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import timber.log.Timber
import timber.log.Timber.DebugTree


class TrackMeApplication : Application(), KodeinAware {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(DebugTree())
    }

    override val kodein = Kodein.lazy {
        import(androidXModule(this@TrackMeApplication))

        bind() from provider { Handler() }
        bind() from singleton { AppPreferenceImpl(instance()) }
        bind() from provider {
            FusedLocationUpdateHelper(
                instance()
            )
        }
        bind() from provider { LocationHelperImpl() }
        bind() from singleton { AppDatabase(instance()) }
        bind() from provider { HistoryViewModelFactory(instance()) }
        bind() from provider { RecordViewModelFactory(instance(), instance(), instance()) }

    }

}