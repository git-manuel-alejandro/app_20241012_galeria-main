package com.iacc.manuelroa_20241012.widgets

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.iacc.manuelroa_20241012.R

class GalleryWidgetProvider : AppWidgetProvider() {

    companion object {
        private val images = listOf(
            R.mipmap.image,
            R.mipmap.image,
            R.mipmap.image,
            R.mipmap.image,
            R.mipmap.image
        )
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    private fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        val views = RemoteViews(context.packageName, R.layout.widget_layout)

        val randomImage = images.random()

        views.setImageViewResource(R.id.widget_image, randomImage)
        views.setTextViewText(R.id.widget_title, "Título de la Imagen")

        appWidgetManager.updateAppWidget(appWidgetId, views)
    }
}
