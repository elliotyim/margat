package com.example.margat.fragment

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.margat.R
import com.example.margat.activity.LoginActivity
import com.example.margat.adapter.MyPhotoRecyclerAdapter
import com.example.margat.config.WebConfig.Companion.ipAddress
import com.example.margat.config.WebConfig.Companion.portNo
import com.example.margat.model.Following
import com.example.margat.model.MyPhotoItem
import com.example.margat.model.Post
import com.example.margat.request.FollowingRequest
import com.example.margat.request.PostingRequest
import com.example.margat.util.MyCallback
import com.example.margat.util.RetrofitAPI
import kotlinx.android.synthetic.main.fragment_profile.*
import retrofit2.Call
import retrofit2.Response


class ProfileFragment : Fragment() {
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: MyPhotoRecyclerAdapter
    private var photoItemList = ArrayList<MyPhotoItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onStart() {
        super.onStart()

        loadUserInfo()
        initialiseMyPhotoList()

        setOnClickListenerToLogoutButton()
    }

    private fun loadUserInfo() {
        var info = activity!!.getSharedPreferences("loginUser", 0)

        profileName.text = info.getString("name", "").toString()
        setProfilePhotoBy(info)
        setFollowingsAndFollowers(info)
        setPostPhotosBy(info)
    }

    private fun initialiseMyPhotoList() {
        mRecyclerView = myPhotosRecycler
        mAdapter = MyPhotoRecyclerAdapter(photoItemList)
        mRecyclerView.adapter = mAdapter

        mRecyclerView.layoutManager = GridLayoutManager(this.context, 3)
    }

    private fun addItem(photoUri: String) {
        photoItemList.add(MyPhotoItem(photoUri))
    }

    private fun setProfilePhotoBy(info: SharedPreferences) {
        Glide.with(activity!!)
            .load("${ipAddress}${portNo}/upload/profile_photos/${info.getString("profilePhoto", "").toString()}")
            .placeholder(R.drawable.profile_default_circle)
            .into(profilePhoto)
        profilePhoto?.background = ShapeDrawable(OvalShape())
        profilePhoto?.clipToOutline = true
    }

    private fun setFollowingsAndFollowers(info: SharedPreferences) {

        var memberNo = info.getInt("no", 0)
        RetrofitAPI.newInstance().getRetrofit().create(FollowingRequest::class.java)
            .findAllFollowingsOf(memberNo).enqueue(object: MyCallback<ArrayList<Following>>() {
            override fun onResponse(
                call: Call<ArrayList<Following>>,
                response: Response<ArrayList<Following>>
            ) {
                if (response.code() == 200) {
                    if (response.body().isNullOrEmpty()) {return}

                    var followings = 0
                    var followers = 0
                    var result: ArrayList<Following> = response.body()!!

                    for (i in result.indices) {
                        if (result[i].followerMemberNo == memberNo)
                            followings++
                        else if (result[i].followedMemberNo == memberNo)
                            followers++
                    }

                    if (numberOfFollowings != null) numberOfFollowings.text = followings.toString()
                    if (numberOfFollowers != null) numberOfFollowers.text = followers.toString()

                    result.clear()
                }
            }

        })
    }

    private fun setPostPhotosBy(info: SharedPreferences) {
        RetrofitAPI.newInstance().getRetrofit().create(PostingRequest::class.java)
            .findAllPostsOf(info.getInt("no", 0)).enqueue(object: MyCallback<ArrayList<Post>>() {
            override fun onResponse(call: Call<ArrayList<Post>>, response: Response<ArrayList<Post>>) {
                if (response.code() == 200) {
                    if (response.body().isNullOrEmpty()) {return}

                    var result = response.body()
                    var numberOfPost = result!!.size
                    setNumberOfPosts(numberOfPost)

                    for (i in 1..numberOfPost) {
                        addItem("${ipAddress}${portNo}/upload/photos/${result[i-1].photos[0].photoName}")
                    }

                    mAdapter.notifyDataSetChanged()
                    result.clear()
                }
            }

        })
    }

    private fun setNumberOfPosts(size: Int?) {
        if (numberOfPost != null) numberOfPost.text = size.toString()
    }

    private fun setOnClickListenerToLogoutButton() {
        logoutButton.setOnClickListener {
            var info: SharedPreferences = this.activity!!.getSharedPreferences("autoLoginUser", 0)
            info.edit().clear().apply()

            val intent = Intent(this.activity, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }
}
