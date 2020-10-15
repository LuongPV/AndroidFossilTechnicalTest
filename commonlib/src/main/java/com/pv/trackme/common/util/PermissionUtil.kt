package com.pv.trackme.common.util

import android.content.Context
import com.pv.trackme.common.callback.DataListener
import com.pv.trackme.common.callback.NoDataListener

object PermissionUtil {

    fun requestPermission(
        context: Context,
        deniedMessage: String,
        permissionGrantListener: NoDataListener,
        permissionDenyListener: DataListener<List<String>>,
        vararg permissions: String
    ) {
        if (permissions.isEmpty()) {
            throw RuntimeException("At least 1 permission required!")
        }
        com.gun0912.tedpermission.TedPermission.with(context)
            .setPermissionListener(object :
                com.gun0912.tedpermission.PermissionListener {
                override fun onPermissionGranted() {
                    permissionGrantListener.invoke()
                }

                override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                    permissionDenyListener.invoke(deniedPermissions!!)
                }

            })
            .setPermissions(*permissions)
            .setDeniedMessage(deniedMessage)
            .check()
    }

}