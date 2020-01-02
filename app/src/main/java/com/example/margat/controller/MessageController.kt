package com.example.margat.controller

import android.content.Context
import com.example.margat.activity.MainActivity
import com.example.margat.adapter.MyMessageRecyclerViewAdapter
import com.example.margat.model.MessageItem
import com.example.margat.request.MessageRequest
import com.example.margat.util.MyCallback
import com.example.margat.util.RetrofitAPI
import retrofit2.Call
import retrofit2.Response

class MessageController {

    private var mActivity: MainActivity
    private var mAdapter: MyMessageRecyclerViewAdapter
    private var mList = ArrayList<MessageItem>()
    private var sumOfUnreadCount = 0


    constructor(context: Context, mList: ArrayList<MessageItem>, mAdapter: MyMessageRecyclerViewAdapter) {
        this.mActivity = context as MainActivity
        this.mList = mList
        this.mAdapter = mAdapter

        loadMessageList()
    }

    fun loadMessageList() {
        var info = mActivity.getSharedPreferences("loginUser", 0)

        var messageRequest = RetrofitAPI().creater.create(MessageRequest::class.java)
        messageRequest.findMessageList(info.getInt("no", 0)).enqueue(object: MyCallback<Array<MessageItem>>() {
            override fun onResponse(
                call: Call<Array<MessageItem>>,
                response: Response<Array<MessageItem>>
            ) {
                if (response.code() == 200) {
                    var resultArr: Array<MessageItem> = response.body()!!
                    sumOfUnreadCount = 0
                    for (e in resultArr) {
                        sumOfUnreadCount += e.unreadMsgCount
                        addItem(e)
                    }
                    mAdapter!!.notifyDataSetChanged()
                }
            }

        })
    }

    private fun addItem(item: MessageItem) {
        mList.add(item)
    }

    fun getSumOfUnreadCount() = sumOfUnreadCount

}