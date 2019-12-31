package com.example.margat.fragment

import android.content.Context
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
import com.example.margat.request.FollowingRequest
import com.example.margat.request.PostingRequest
import com.example.margat.domain.Following
import com.example.margat.domain.Post
import com.example.margat.item.MyPhotoItem
import com.example.margat.util.MyCallback
import com.example.margat.util.RetrofitAPI
import kotlinx.android.synthetic.main.fragment_profile.*
import retrofit2.Call
import retrofit2.Response


class ProfileFragment : Fragment() {
    private lateinit var mRecyclerVIew: RecyclerView
    private lateinit var mAdapter: MyPhotoRecyclerAdapter
    private var mList = ArrayList<MyPhotoItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onStart() {
        super.onStart()
        mContext = activity!!.applicationContext
        loadUserInfo()

        mRecyclerVIew = myPhotosRecycler
        mAdapter = MyPhotoRecyclerAdapter(mList)
        mRecyclerVIew.adapter = mAdapter

        mRecyclerVIew.layoutManager = GridLayoutManager(this.context, 3)

        logoutButton.setOnClickListener {
            var info: SharedPreferences = this.activity!!.getSharedPreferences("setting", 0)
            var editor: SharedPreferences.Editor = info.edit()
            editor.clear()
            editor.apply()

            val intent = Intent(this.activity, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    companion object {
        lateinit var mContext: Context
    }

    private fun addItem(photoUri: String) {
        var item = MyPhotoItem()
        item.photoUri = photoUri
        mList.add(item)
    }

    private fun loadUserInfo() {
        var info = mContext.getSharedPreferences("loginUser", 0)

        profileName.text = info.getString("name", "").toString()
        setProfilePhotoBy(info)
        setFollowingsAndFollowers(info)
        setPostPhotosBy(info)

    }

    private fun setProfilePhotoBy(info: SharedPreferences) {
        var profilePhotoFileName = info.getString("profilePhoto", "").toString()
        Glide.with(mContext)
            .load("${ipAddress}:${portNo}/upload/profile_photos/${profilePhotoFileName}")
            .placeholder(R.drawable.profile_default_circle)
            .into(profilePhoto)
        profilePhoto?.background = ShapeDrawable(OvalShape())
        profilePhoto?.clipToOutline = true
    }

    private fun setFollowingsAndFollowers(info: SharedPreferences) {
        var followingController = RetrofitAPI().creater.create(FollowingRequest::class.java)

        var memberNo = info.getInt("no", 0)

        followingController.findAllFollowingsOf(memberNo).enqueue(object: MyCallback<Array<Following>>() {
            override fun onResponse(
                call: Call<Array<Following>>,
                response: Response<Array<Following>>
            ) {
                if (response.code() == 200) {
                    if (response.body().isNullOrEmpty()) {return}

                    var followings = 0
                    var followers = 0
                    var result: Array<Following> = response.body()!!

                    for (i in result.indices) {
                        if (result[i].followerMemberNo == memberNo)
                            followings++
                        else if (result[i].followedMemberNo == memberNo)
                            followers++
                    }

                    if (numberOfFollowings != null) numberOfFollowings.text = followings.toString()
                    if (numberOfFollowers != null) numberOfFollowers.text = followers.toString()

                }
            }

        })
    }

    private fun setPostPhotosBy(info: SharedPreferences) {
        var postingController = RetrofitAPI().creater.create(PostingRequest::class.java)

        postingController.findAllPostsOf(info.getInt("no", 0)).enqueue(object: MyCallback<Array<Post>>() {
            override fun onResponse(call: Call<Array<Post>>, response: Response<Array<Post>>) {
                if (response.code() == 200) {
                    var result = response.body()
                    var numberOfPost = result?.size
                    setNumberOfPosts(numberOfPost)
                    if (response.body().isNullOrEmpty()) {return}

                    for (i in 1..numberOfPost!!) {
                        var photoName = result!![i-1].photos[0].photoName
                        addItem("${ipAddress}:${portNo}/upload/photos/${photoName}")
                    }

                    mAdapter.notifyDataSetChanged()
                }
            }

        })
    }

    private fun setNumberOfPosts(size: Int?) {
        if (numberOfPost != null) numberOfPost.text = size.toString()
    }
}
