package com.example.margat.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.margat.R
import com.example.margat.activity.MainActivity
import com.example.margat.adapter.MessageViewPagerAdapter
import com.example.margat.adapter.MyMessageRecyclerViewAdapter
import com.example.margat.util.NonSwipeViewPager
import kotlinx.android.synthetic.main.fragment_message_viewpager.*

class MessageViewPagerFragment: Fragment() {

    private var mListener: OnMessageViewPagerFragmentInteractionListener? = null

    private var mNum: Int = 0

    private lateinit var mMessageViewPager: NonSwipeViewPager
    private lateinit var mMessageViewPagerAdapter: MessageViewPagerAdapter

    interface OnMessageViewPagerFragmentInteractionListener {
        fun onMyMessageFragmentInteraction(item: NonSwipeViewPager)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnMessageViewPagerFragmentInteractionListener) {
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
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_message_viewpager, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mMessageViewPager = messageViewPager
        mMessageViewPagerAdapter = MessageViewPagerAdapter(childFragmentManager, mListener as MainActivity)
        mMessageViewPager.adapter = mMessageViewPagerAdapter
        mListener!!.onMyMessageFragmentInteraction(mMessageViewPager)
    }

}