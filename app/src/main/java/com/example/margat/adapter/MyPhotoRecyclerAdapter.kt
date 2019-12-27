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
import com.example.margat.fragment.ProfileFragment
import com.example.margat.item.MyPhotoItem

class MyPhotoRecyclerAdapter: RecyclerView.Adapter<ViewHolder> {

    private var mData: ArrayList<MyPhotoItem>

    constructor(list: ArrayList<MyPhotoItem>) {
        mData = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var context = parent.context
        var inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        var view = inflater.inflate(R.layout.my_photo_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var item = mData[position]
        Glide.with(ProfileFragment.mContext)
            .load(item.photoUri)
            .into(holder.photo)


    }

    override fun getItemCount(): Int {
        return mData.size
    }

    inner class ViewHolder: RecyclerView.ViewHolder {
        var photo: ImageView

        constructor(itemView: View) : super(itemView) {
            photo = itemView.findViewById(R.id.myPhoto)
        }
    }


}