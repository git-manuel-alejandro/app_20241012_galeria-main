package com.iacc.manuelroa_20241012.helpers

import android.content.Context
import androidx.appcompat.app.AlertDialog

object DialogHelper {
    fun showNotificationDialog(context: Context, message: String) {
        AlertDialog.Builder(context)
            .setTitle("Notificación")
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }
}
