package com.example.margat.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.margat.R
import com.example.margat.activity.MainActivity
import com.example.margat.adapter.MyMessageRecyclerViewAdapter
import com.example.margat.model.MyMessageItem
import kotlinx.android.synthetic.main.fragment_message_list.*

class MessageListFragment: Fragment() {
    interface OnMessageListFragmentInteractionListener {
        fun onMessageRecyclerViewAdapter(viewAdapter: MyMessageRecyclerViewAdapter)
        fun onMessageListFragmentInteraction(messageItem: MyMessageItem.MessageItem)
        fun onAnotherInteraction(button: Button)
    }

    private var mListener: OnMessageListFragmentInteractionListener? = null
    private lateinit var mMessageListRecyclerViewAdapter: MyMessageRecyclerViewAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnMessageListFragmentInteractionListener) {
            mListener = context as MainActivity

            mMessageListRecyclerViewAdapter = MyMessageRecyclerViewAdapter(mListener!!)
            mListener!!.onMessageRecyclerViewAdapter(mMessageListRecyclerViewAdapter)

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

        var view = inflater.inflate(R.layout.fragment_message_list, container, false)
        mListener!!.onAnotherInteraction(view.findViewById(R.id.temp_button_to_detail) as Button)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        messageListRecycler.adapter = mMessageListRecyclerViewAdapter
        messageListRecycler.layoutManager = LinearLayoutManager(activity)



    }

}