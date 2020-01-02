package com.example.margat.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import com.example.margat.activity.MainActivity
import com.example.margat.fragment.FeedFragment
import com.example.margat.fragment.MessageDetailFragment
import com.example.margat.fragment.MessageListFragment
import com.example.margat.fragment.ProfileFragment
import com.example.margat.model.MessageItem

class MessageViewPagerAdapter: FragmentStatePagerAdapter {

    private var mActivity: MainActivity

    private var pageIndexes: ArrayList<Int> = ArrayList()

    constructor(fm: FragmentManager, mActivity: MainActivity): super(fm) {
        this.mActivity = mActivity
        for (i in 0..1)
            pageIndexes.add(i)
    }

    override fun getCount(): Int = pageIndexes.size

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> MessageListFragment()
            1 -> MessageDetailFragment()
            else -> Fragment()
        }
    }

    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }

}