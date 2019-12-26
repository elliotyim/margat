package com.example.margat.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.margat.R
import com.example.margat.adapter.ContentsPagerAdapter
import com.example.margat.fragment.FeedFragment
import com.example.margat.fragment.MessageFragment
import com.example.margat.fragment.ProfileFragment
import com.example.margat.item.FeedContent
import com.example.margat.item.MessageContent
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.TabLayoutOnPageChangeListener
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(),
    ProfileFragment.OnFragmentInteractionListener,
    MessageFragment.OnListFragmentInteractionListener,
    FeedFragment.OnListFragmentInteractionListener{

    private lateinit var mContext: Context
    private lateinit var mTabLayout: TabLayout

    private lateinit var mViewPager: ViewPager
    private lateinit var mContentPagerAdapter: ContentsPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mContext = applicationContext

        mTabLayout = layout_tab as TabLayout
        mTabLayout.addTab(mTabLayout.newTab().setIcon(R.drawable.ic_home_solid))
        mTabLayout.addTab(mTabLayout.newTab().setIcon(R.drawable.ic_plus_square_regular))
        mTabLayout.addTab(mTabLayout.newTab().setIcon(R.drawable.ic_paper_plane_regular))
        mTabLayout.addTab(mTabLayout.newTab().setIcon(R.drawable.ic_user_regular))

        mViewPager = pager_content
        mContentPagerAdapter =
            ContentsPagerAdapter(supportFragmentManager, mTabLayout.tabCount)
        mViewPager.adapter = mContentPagerAdapter
        mViewPager.addOnPageChangeListener(TabLayoutOnPageChangeListener(mTabLayout))

        mTabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                mViewPager.currentItem = tab?.position!!
                when (tab.position) {
                    0 -> tab.icon = applicationContext.getDrawable(R.drawable.ic_home_solid)
                    1 -> tab.icon = applicationContext.getDrawable(R.drawable.ic_plus_square_solid)
                    2 -> tab.icon = applicationContext.getDrawable(R.drawable.ic_paper_plane_solid)
                    3 -> tab.icon = applicationContext.getDrawable(R.drawable.ic_user_solid)
                }

            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> tab.icon = applicationContext.getDrawable(R.drawable.ic_home_regular)
                    1 -> tab.icon = applicationContext.getDrawable(R.drawable.ic_plus_square_regular)
                    2 -> tab.icon = applicationContext.getDrawable(R.drawable.ic_paper_plane_regular)
                    3 -> tab.icon = applicationContext.getDrawable(R.drawable.ic_user_regular)
                }
            }
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

    }



    override fun onListFragmentInteraction(item: FeedContent.FeedItem?) {}
    override fun onListFragmentInteraction(item: MessageContent.MessageItem?) {}
    override fun onFragmentInteraction(uri: Uri) {}
}
