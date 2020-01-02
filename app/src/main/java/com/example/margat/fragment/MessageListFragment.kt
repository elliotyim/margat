package com.example.margat.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.margat.R
import com.example.margat.activity.MainActivity
import com.example.margat.adapter.MyMessageRecyclerViewAdapter
import com.example.margat.controller.MessageController
import com.example.margat.model.MessageItem
import kotlinx.android.synthetic.main.fragment_message_list.*

class MessageListFragment: Fragment() {
    private var mListener: MainActivity? = null
    private var mList = ArrayList<MessageItem>()

    private lateinit var mMessageListRecycler: RecyclerView
    private lateinit var mMessageListRecyclerViewAdapter: MyMessageRecyclerViewAdapter

    private lateinit var mMessageController: MessageController

    interface OnMessageListFragmentInteractionListener {
        fun getMessageRecyclerViewAdapter(adapter: MyMessageRecyclerViewAdapter)
        fun getMessageController(controller: MessageController)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnMessageListFragmentInteractionListener) {
            mListener = context as MainActivity?

            mMessageListRecyclerViewAdapter = MyMessageRecyclerViewAdapter(mList, mListener!!)
            mMessageController = MessageController(mListener!!, mList, mMessageListRecyclerViewAdapter)

            mListener!!.getMessageRecyclerViewAdapter(mMessageListRecyclerViewAdapter)
            mListener!!.getMessageController(mMessageController!!)
        } else {
            throw RuntimeException("$context must implement OnListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_message_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mMessageListRecycler = messageListRecycler
        mMessageListRecycler.adapter = mMessageListRecyclerViewAdapter
        mMessageListRecycler.layoutManager = LinearLayoutManager(mListener!!)
    }

}