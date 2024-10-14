package com.iacc.manuelroa_20241012

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView


class ImageAdapter(private val images: List<Int>) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {


    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imageRes = images[position]
        holder.imageView.setImageResource(imageRes)
    }

    override fun getItemCount() = images.size
}
