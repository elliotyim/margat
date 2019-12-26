package com.example.margat.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.margat.fragment.FeedFragment
import com.example.margat.fragment.ProfileFragment
import com.example.margat.fragment.MessageFragment
import com.example.margat.fragment.PostingFragment

class ContentsPagerAdapter: FragmentStatePagerAdapter {
    private var mPageCount: Int

    constructor(fm: FragmentManager, pageCount: Int): super(fm) {
        this.mPageCount = pageCount
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> FeedFragment()
            1 -> PostingFragment()
            2 -> MessageFragment()
            3 -> ProfileFragment()
            else -> null as Fragment
        }
    }

    override fun getCount(): Int {
        return mPageCount
    }


}