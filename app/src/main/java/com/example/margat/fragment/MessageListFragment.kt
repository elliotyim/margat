package com.example.margat.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.margat.R
import com.example.margat.activity.MainActivity
import com.example.margat.adapter.MyMessageRecyclerViewAdapter
import com.example.margat.controller.MessageController
import com.example.margat.model.MessageItem
import com.example.margat.util.NonSwipeViewPager
import kotlinx.android.synthetic.main.fragment_message_list.*

class MessageListFragment: Fragment() {
    private var mListener: OnMessageListFragmentInteractionListener? = null
    private var mList = ArrayList<MessageItem>()

    private lateinit var mMessageListRecycler: RecyclerView
    private lateinit var mMessageListRecyclerViewAdapter: MyMessageRecyclerViewAdapter

    private lateinit var mMessageController: MessageController

    private lateinit var mMessageViewpager: NonSwipeViewPager

    private lateinit var mButton: Button

    interface OnMessageListFragmentInteractionListener {
        fun getMessageRecyclerViewAdapter(adapter: MyMessageRecyclerViewAdapter)
        fun getMessageController(controller: MessageController)
        fun onMessageListFragmentInteraction(item: MessageItem)
        fun onAnotherInteraction(button: Button)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnMessageListFragmentInteractionListener) {
            mListener = context as MainActivity?

            mMessageListRecyclerViewAdapter = MyMessageRecyclerViewAdapter(mList, mListener!!)
            mMessageController = MessageController(mListener as MainActivity, mList, mMessageListRecyclerViewAdapter)

            mListener!!.getMessageRecyclerViewAdapter(mMessageListRecyclerViewAdapter)
            mListener!!.getMessageController(mMessageController)


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
        Log.w("asdf", "여기가 먼저 실행될까?")
        var view = inflater.inflate(R.layout.fragment_message_list, container, false)
        var button = view.findViewById(R.id.temp_button_to_detail) as Button
        mListener!!.onAnotherInteraction(button)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mMessageListRecycler = messageListRecycler
        mMessageListRecycler.adapter = mMessageListRecyclerViewAdapter
        mMessageListRecycler.layoutManager = LinearLayoutManager(mListener!! as MainActivity)



    }

}