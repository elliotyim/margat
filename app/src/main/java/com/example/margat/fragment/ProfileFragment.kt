package com.example.margat.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.net.Uri
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
import com.example.margat.controller.PostingController
import com.example.margat.domain.Post
import com.example.margat.item.MyPhotoItem
import com.example.margat.util.MyCallback
import com.example.margat.util.RetrofitAPI
import kotlinx.android.synthetic.main.fragment_profile.*
import retrofit2.Call
import retrofit2.Response


class ProfileFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null

    private lateinit var mRecyclerVIew: RecyclerView
    private lateinit var mAdapter: MyPhotoRecyclerAdapter
    private var mList = ArrayList<MyPhotoItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

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

    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
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
        var controller = RetrofitAPI().creater.create(PostingController::class.java)

        var info = mContext.getSharedPreferences("loginUser", 0)
        profileName.text = info.getString("name", "").toString()

        var profilePhotoFileName = info.getString("profilePhoto", "").toString()
        Glide.with(mContext)
            .load("${ipAddress}:${portNo}/upload/profile_photos/${profilePhotoFileName}")
            .into(profilePhoto)
        profilePhoto?.background = ShapeDrawable(OvalShape())
        profilePhoto?.clipToOutline = true

        controller.findAllPostsOf(info.getInt("no", 0)).enqueue(object: MyCallback<Array<Post>>() {
            override fun onResponse(call: Call<Array<Post>>, response: Response<Array<Post>>) {
                if (response.code() == 200) {
                    if (response.body().isNullOrEmpty()) {
                        return
                    }
                    for (i in 1..response.body()?.size!!)
                        addItem("${ipAddress}:${portNo}/upload/photos/b.jpg")
                    mAdapter.notifyDataSetChanged()
                }
            }

        })

    }

}
