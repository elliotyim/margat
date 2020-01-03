package com.example.margat.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.margat.R
import com.example.margat.adapter.MyPhotoRecyclerAdapter.ViewHolder
import com.example.margat.model.MyPhotoItem
import com.example.margat.util.App

class MyPhotoRecyclerAdapter: RecyclerView.Adapter<ViewHolder> {

    private var photoItems: ArrayList<MyPhotoItem>

    constructor(list: ArrayList<MyPhotoItem>) {
        photoItems = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var inflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return ViewHolder(inflater.inflate(R.layout.my_photo_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(App.MyApp.context)
            .load(photoItems[position].photoUri)
            .into(holder.photo)

    }

    override fun getItemCount(): Int = photoItems.size

    inner class ViewHolder: RecyclerView.ViewHolder {
        var photo: ImageView

        constructor(itemView: View) : super(itemView) {
            photo = itemView.findViewById(R.id.myPhoto)
        }
    }


}