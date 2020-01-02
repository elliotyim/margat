package com.example.margat.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.margat.R
import com.example.margat.activity.MainActivity
import com.example.margat.adapter.MessageViewPagerAdapter
import com.example.margat.adapter.MyMessageRecyclerViewAdapter
import com.example.margat.model.MessageItem
import com.example.margat.util.NonSwipeViewPager
import kotlinx.android.synthetic.main.fragment_message_viewpager.*

class MessageViewPagerFragment: Fragment() {

    private var mListener: OnMyMessageFragmentInteractionListener? = null

    private var mNum: Int = 0
    private var mList = ArrayList<MessageItem>()

    private lateinit var mMessageViewPager: NonSwipeViewPager
    private lateinit var mMessageViewPagerAdapter: MessageViewPagerAdapter

    private lateinit var mMessageListRecyclerView: RecyclerView
    private lateinit var mMessageListAdapter: MyMessageRecyclerViewAdapter

    interface OnMyMessageFragmentInteractionListener {
        fun onMyMessageFragmentInteraction(item: MyMessageRecyclerViewAdapter)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnMyMessageFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException("$context must implement OnListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mNum = if (arguments != null) arguments!!.getInt("num") else 1

//        mMessageListAdapter = MyMessageRecyclerViewAdapter(mList, mContext)

//        var messageController = MessageController(mListener as MainActivity, mList, mMessageListAdapter)
//        messageController.loadMessageList()

//        mListener!!.onMyMessageFragmentInteraction(mMessageListAdapter)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_message_viewpager, container, false)
    }

    override fun onStart() {
        super.onStart()
        initialiseMessageListViewPager()
    }

    private fun initialiseMessageListViewPager() {
        mMessageViewPager = messageViewPager
        mMessageViewPagerAdapter = MessageViewPagerAdapter(fragmentManager!!, mList, mListener as MainActivity)
        mMessageViewPager.adapter = mMessageViewPagerAdapter
    }


}