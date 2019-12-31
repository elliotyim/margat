package com.example.margat.controller

import androidx.viewpager.widget.ViewPager
import com.example.margat.activity.MainActivity
import com.example.margat.adapter.ContentsPagerAdapter
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*

class ContentViewPagerController {

    var mActivity: MainActivity

    constructor(mainActivity: MainActivity) {
        mActivity = mainActivity
    }

    fun createContentViewPagerWith(mTabLayout: TabLayout): ViewPager {
        var mContentsViewPager = mActivity.pager_content
        var mContentPagerAdapter =
            ContentsPagerAdapter(mActivity.supportFragmentManager, mTabLayout.tabCount)

        mContentsViewPager.adapter = mContentPagerAdapter
        mContentsViewPager.addOnPageChangeListener(
            TabLayout.TabLayoutOnPageChangeListener(mTabLayout)
        )

        return mContentsViewPager!!
    }
}