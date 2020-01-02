package com.example.margat.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.margat.R
import com.example.margat.activity.MainActivity
import com.example.margat.adapter.MyMessageRecyclerViewAdapter
import com.example.margat.controller.MessageController

import com.example.margat.item.MessageItem
import kotlinx.android.synthetic.main.fragment_message_list.*

class MessageFragment : Fragment() {

    private var listener: OnListFragmentInteractionListener? = null

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: MyMessageRecyclerViewAdapter
    private var mList = ArrayList<MessageItem>()

    interface OnListFragmentInteractionListener {
        fun onListFragmentInteraction(item: MessageItem?)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_message_list, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAdapter = MyMessageRecyclerViewAdapter(mList, activity!!.applicationContext)
        var messageController = MessageController(listener as MainActivity, mList, mAdapter)
        messageController.loadMessageList()
    }

    override fun onStart() {
        super.onStart()

        mRecyclerView = messageListRecycler
        mRecyclerView.adapter = mAdapter

        mRecyclerView.layoutManager = LinearLayoutManager(this.context)
    }




}
