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
import com.example.margat.fragment.MessageListFragment
import com.example.margat.model.MyMessageItem
import com.example.margat.request.MessageRequest
import com.example.margat.util.App
import com.example.margat.util.MyCallback
import com.example.margat.util.RetrofitAPI
import retrofit2.Call
import retrofit2.Response

class MyMessageRecyclerViewAdapter : RecyclerView.Adapter<MyMessageRecyclerViewAdapter.ViewHolder> {

    private var messageItemList: ArrayList<MyMessageItem.MessageItem> = MyMessageItem.messageItemList
    private val mOnClickListener: View.OnClickListener

    constructor(mListener: MessageListFragment.OnMessageListFragmentInteractionListener?) {
        loadMessageList()
        mOnClickListener = View.OnClickListener { v ->
            mListener!!.onMessageListFragmentInteraction(v.tag as MyMessageItem.MessageItem)
        }

    }

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var inflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return ViewHolder(inflater.inflate(R.layout.fragment_message_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var item = messageItemList[position]

        Glide.with(App.MyApp.context)
            .load("${WebConfig.ipAddress}${WebConfig.portNo}/upload/profile_photos/${item.messageUserPhotoItem}")
            .placeholder(R.drawable.profile_default_circle)
            .into(holder.messageUserPhotoItem)

        with (holder.messageUserPhotoItem) {
            scaleType = ImageView.ScaleType.CENTER_CROP
            background = ShapeDrawable(OvalShape())
            clipToOutline = true
        }

        with (holder) {
            userName.text = item.userName
            receivedDate.text = item.receivedDate.toString()
            messageContent.text = item.messageContent
            if (item.unreadMsgCount != 0) unreadMsgCount.text = item.unreadMsgCount.toString()
        }

        with(holder.itemView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }

    }

    override fun getItemCount(): Int = messageItemList.size

    private fun loadMessageList() {
        var info = App.MyApp.context.getSharedPreferences("loginUser", 0)
        messageItemList.clear()

        RetrofitAPI.newInstance().getRetrofit().create(MessageRequest::class.java)
            .findMessageList(info.getInt("no", 0)).enqueue(object: MyCallback<ArrayList<MyMessageItem.MessageItem>>() {
                override fun onResponse(
                    call: Call<ArrayList<MyMessageItem.MessageItem>>,
                    response: Response<ArrayList<MyMessageItem.MessageItem>>
                ) {
                    if (response.code() == 200) {
                        var resultArr: ArrayList<MyMessageItem.MessageItem> = response.body()!!
                        var sumOfUnreadCount = 0
                        for (e in resultArr) {
                            sumOfUnreadCount += e.unreadMsgCount
                            addItem(e)
                        }
                        notifyDataSetChanged()
                        resultArr.clear()
                    }
                }

            })
    }

    private fun addItem(item: MyMessageItem.MessageItem) {
        messageItemList.add(item)
    }

}
