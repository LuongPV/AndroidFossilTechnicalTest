package com.pv.trackme.util

import android.content.Context
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission

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
        TedPermission.with(context)
            .setPermissionListener(object : PermissionListener {
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