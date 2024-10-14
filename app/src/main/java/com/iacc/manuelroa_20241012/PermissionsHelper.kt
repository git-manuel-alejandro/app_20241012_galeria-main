package com.iacc.manuelroa_20241012.helpers

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.iacc.manuelroa_20241012.MainActivity

class PermissionsHelper(private val activity: MainActivity) {

    companion object {
        const val NOTIFICATION_REQUEST_CODE = 100
    }

    fun checkNotificationPermissions(onPermissionGranted: () -> Unit) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            AlertDialog.Builder(activity)
                .setTitle("Permisos de Notificación")
                .setMessage("¿Desea habilitar las notificaciones para esta aplicación?")
                .setPositiveButton("Sí") { _, _ ->
                    ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.POST_NOTIFICATIONS), NOTIFICATION_REQUEST_CODE)
                }
                .setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
        } else {
            onPermissionGranted()
        }
    }

    fun handlePermissionsResult(requestCode: Int, grantResults: IntArray) {
        if (requestCode == NOTIFICATION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {

                AlertDialog.Builder(activity)
                    .setTitle("Permisos Denegados")
                    .setMessage("No se han habilitado las notificaciones.")
                    .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                    .create()
                    .show()
            }
        }
    }
}
