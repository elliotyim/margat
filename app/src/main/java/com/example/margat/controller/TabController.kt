package com.example.margat.controller

import androidx.viewpager.widget.ViewPager
import com.example.margat.R
import com.example.margat.activity.MainActivity
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*

class TabController {

    lateinit var mTabLayout: TabLayout
    var mActivity: MainActivity

    constructor(mainActivity: MainActivity) {
        mActivity = mainActivity
    }

    fun createTabLayout(): TabLayout {
        mTabLayout = mActivity.layout_tab as TabLayout
        mTabLayout.addTab(mTabLayout.newTab().setIcon(R.drawable.ic_home_solid))
        mTabLayout.addTab(mTabLayout.newTab().setIcon(R.drawable.ic_plus_square_regular))
        mTabLayout.addTab(mTabLayout.newTab().setIcon(R.drawable.ic_paper_plane_regular))
        mTabLayout.addTab(mTabLayout.newTab().setIcon(R.drawable.ic_user_regular))
        return mTabLayout
    }

    fun setEventListenersOnTabsWith(mContentsViewPager: ViewPager) {
        mTabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                mContentsViewPager.currentItem = tab?.position!!
                when (tab.position) {
                    0 -> tab.icon = mActivity.getDrawable(R.drawable.ic_home_solid)
                    1 -> tab.icon = mActivity.getDrawable(R.drawable.ic_plus_square_solid)
                    2 -> tab.icon = mActivity.getDrawable(R.drawable.ic_paper_plane_solid)
                    3 -> tab.icon = mActivity.getDrawable(R.drawable.ic_user_solid)
                }

            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> tab.icon = mActivity.getDrawable(R.drawable.ic_home_regular)
                    1 -> tab.icon = mActivity.getDrawable(R.drawable.ic_plus_square_regular)
                    2 -> tab.icon = mActivity.getDrawable(R.drawable.ic_paper_plane_regular)
                    3 -> tab.icon = mActivity.getDrawable(R.drawable.ic_user_regular)
                }
            }
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }
}