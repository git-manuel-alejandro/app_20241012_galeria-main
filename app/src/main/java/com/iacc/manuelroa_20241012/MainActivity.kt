package com.iacc.manuelroa_20241012

import android.Manifest
import android.app.PendingIntent
import android.app.NotificationChannel
import android.app.NotificationManager
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iacc.manuelroa_20241012.widgets.GalleryWidgetProvider

class MainActivity : AppCompatActivity() {

    companion object {
        const val CHANNEL_ID = "default_channel"
        const val NOTIFICATION_ID = 1
        const val NOTIFICATION_REQUEST_CODE = 100
        const val PREFS_NAME = "app_preferences"
        const val WIDGET_ADDED_KEY = "widget_added"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        createNotificationChannel()


        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 2)


        val images = listOf(
            R.mipmap.image,
            R.mipmap.image,
            R.mipmap.image,
            R.mipmap.image,
            R.mipmap.image
        )

        recyclerView.adapter = ImageAdapter(images)


        if (intent?.getBooleanExtra("from_notification", false) == true) {
            showNotificationDialog()        }


        askToAddWidgetIfFirstTime()

        val notifyButton = findViewById<com.google.android.material.button.MaterialButton>(R.id.notifyButton)
        notifyButton.setOnClickListener {
            checkNotificationPermissions()
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Default Channel"
            val descriptionText = "Channel for default notifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun checkNotificationPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {

            AlertDialog.Builder(this)
                .setTitle("Permisos de Notificación")
                .setMessage("¿Desea habilitar las notificaciones para esta aplicación?")
                .setPositiveButton("Sí") { _, _ ->

                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), NOTIFICATION_REQUEST_CODE)
                }
                .setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
        } else {

            sendNotification()
        }
    }

    private fun sendNotification() {

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("from_notification", true)
        }

        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)


        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.mipmap.image)
            .setContentTitle("APP Galería")
            .setContentText("APP Galería Contenido.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)) {
            notify(NOTIFICATION_ID, builder.build())
        }
    }

    private fun showNotificationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Notificación")
            .setMessage("Notificaciones funcionando")
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }

    private fun askToAddWidgetIfFirstTime() {
        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val widgetAdded = prefs.getBoolean(WIDGET_ADDED_KEY, false)

        if (!widgetAdded) {

            AlertDialog.Builder(this)
                .setTitle("Agregar Widget")
                .setMessage("¿Desea agregar el widget de la galería a su pantalla de inicio?")
                .setPositiveButton("Sí") { _, _ ->

                    addWidgetToHomeScreen()


                    prefs.edit().putBoolean(WIDGET_ADDED_KEY, true).apply()
                }
                .setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
        }
    }

    private fun addWidgetToHomeScreen() {
        val appWidgetManager = AppWidgetManager.getInstance(this)
        val myComponent = ComponentName(this, GalleryWidgetProvider::class.java)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (appWidgetManager.isRequestPinAppWidgetSupported) {
                val pinnedWidgetCallbackIntent = PendingIntent.getBroadcast(
                    this, 0, Intent(this, GalleryWidgetProvider::class.java),
                    PendingIntent.FLAG_IMMUTABLE
                )
                appWidgetManager.requestPinAppWidget(myComponent, null, pinnedWidgetCallbackIntent)
                Toast.makeText(this, "Widget agregado a la pantalla de inicio", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "No se puede agregar el widget automáticamente", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == NOTIFICATION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                sendNotification()
            } else {

                AlertDialog.Builder(this)
                    .setTitle("Permisos Denegados")
                    .setMessage("No se han habilitado las notificaciones.")
                    .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                    .create()
                    .show()
            }
        }
    }
}
