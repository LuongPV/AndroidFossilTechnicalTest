package com.pv.trackme.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein

abstract class BaseActivity : AppCompatActivity(), KodeinAware {
    override val kodein by kodein()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        initViews()
    }

    abstract fun getLayoutId(): Int

    abstract fun initViews()

}