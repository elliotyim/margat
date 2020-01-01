package com.example.margat.adapter


import android.content.Context
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.margat.R
import com.example.margat.config.WebConfig
import com.example.margat.fragment.ProfileFragment
import com.example.margat.item.MessageItem
import kotlinx.android.synthetic.main.fragment_profile.*

class MyMessageRecyclerViewAdapter : RecyclerView.Adapter<MyMessageRecyclerViewAdapter.ViewHolder> {

    private var mData: List<MessageItem>
    private var mContext: Context

    constructor(list: List<MessageItem>, mContext: Context) {
        this.mData = list
        this.mContext = mContext
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var context = parent.context
        var inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        var view = inflater.inflate(R.layout.fragment_message, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var item = mData[position]

        Glide.with(mContext)
            .load("${WebConfig.ipAddress}${WebConfig.portNo}/upload/profile_photos/${item.messageUserPhotoItem}")
            .placeholder(R.drawable.profile_default_circle)
            .into(holder.messageUserPhotoItem)
        holder.messageUserPhotoItem.scaleType = ImageView.ScaleType.CENTER_CROP
        holder.messageUserPhotoItem.background = ShapeDrawable(OvalShape())
        holder.messageUserPhotoItem.clipToOutline = true

        holder.userName.text = item.userName
        if (item.unreadMsgCount != 0)
            holder.unreadMsgCount.text = item.unreadMsgCount.toString()
        holder.receivedDate.text = item.receivedDate.toString()
        holder.messageContent.text = item.messageContent
    }

    override fun getItemCount(): Int = mData.size

    inner class ViewHolder: RecyclerView.ViewHolder {
        var messageUserPhotoItem: ImageView
        var userName: TextView
        var unreadMsgCount: TextView
        var receivedDate: TextView
        var messageContent: TextView

        constructor(itemView: View): super(itemView) {
            messageUserPhotoItem = itemView.findViewById(R.id.messageUserPhotoItem)
            userName = itemView.findViewById(R.id.userName)
            unreadMsgCount = itemView.findViewById(R.id.unreadMsgCount)
            receivedDate = itemView.findViewById(R.id.receivedDate)
            messageContent = itemView.findViewById(R.id.messageContent)

        }
    }

}
