package com.example.margat.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.margat.R
import com.example.margat.adapter.MessageViewPagerAdapter
import com.example.margat.util.NonSwipeViewPager
import kotlinx.android.synthetic.main.fragment_message_viewpager.*

class MessageViewPagerFragment: Fragment() {

    private var mListener: OnMessageViewPagerFragmentInteractionListener? = null

    interface OnMessageViewPagerFragmentInteractionListener {
        fun onMyMessageFragmentInteraction(nonSwipeViewPager: NonSwipeViewPager)
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

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_message_viewpager, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        messageViewPager.adapter = MessageViewPagerAdapter(childFragmentManager)
        mListener!!.onMyMessageFragmentInteraction(messageViewPager)
    }

}