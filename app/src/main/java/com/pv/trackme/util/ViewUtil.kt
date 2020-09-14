package com.pv.trackme.util

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.pv.trackme.R

object ViewUtil {

    fun showLoading(context: Context): AlertDialog {
        val alertDialog = AlertDialog.Builder(context).setMessage(context.getString(R.string.msg_loading)).create()
        alertDialog.show()
        return alertDialog
    }

}