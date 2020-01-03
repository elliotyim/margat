package com.example.margat.controller

import androidx.viewpager.widget.ViewPager
import com.example.margat.R
import com.example.margat.activity.MainActivity
import com.example.margat.model.MyMessageItem
import com.example.margat.request.MessageRequest
import com.example.margat.util.App
import com.example.margat.util.MyCallback
import com.example.margat.util.RetrofitAPI
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Response

class TabController {

    private lateinit var mTabLayout: TabLayout
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
    fun setUnreadMessageCountOnCreateActivity() {
        val badge: BadgeDrawable = mTabLayout.getTabAt(2)!!.orCreateBadge
        badge.isVisible = true

        var info = App.MyApp.context.getSharedPreferences("loginUser", 0)

        RetrofitAPI.newInstance().getRetrofit().create(MessageRequest::class.java)
            .findMessageList(info.getInt("no", 0))
            .enqueue(object : MyCallback<ArrayList<MyMessageItem.MessageItem>>() {
                override fun onResponse(
                    call: Call<ArrayList<MyMessageItem.MessageItem>>,
                    response: Response<ArrayList<MyMessageItem.MessageItem>>
                ) {
                    if (response.code() == 200) {
                        var resultArr: ArrayList<MyMessageItem.MessageItem> = response.body()!!
                        var sumOfUnreadCount = 0
                        for (e in resultArr) {
                            sumOfUnreadCount += e.unreadMsgCount
                        }
                        badge.number = sumOfUnreadCount
                        if (sumOfUnreadCount == 0) mTabLayout.getTabAt(2)!!.removeBadge()

                        resultArr.clear()
                    }
                }

            })
    }

}