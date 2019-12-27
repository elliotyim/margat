package com.example.margat.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.FutureTarget
import com.example.margat.R
import com.example.margat.activity.LoginActivity
import com.example.margat.adapter.MyPhotoRecyclerAdapter
import com.example.margat.item.MyPhotoItem
import kotlinx.android.synthetic.main.fragment_profile.*
import java.net.URL
import java.util.concurrent.Executors

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ProfileFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    private var profileImage: ImageView? = null
    private var threadPool = Executors.newFixedThreadPool(3)

    private lateinit var mRecyclerVIew: RecyclerView
    private lateinit var mAdapter: MyPhotoRecyclerAdapter
    private var mList = ArrayList<MyPhotoItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        profileImage = profilePhoto
        ProfilePhotoProcessor().executeOnExecutor(threadPool, "http://192.168.0.125:8080/upload/profile_photos/a.jpg")

        mRecyclerVIew = myPhotosRecycler
        mAdapter = MyPhotoRecyclerAdapter(mList)
        mRecyclerVIew.adapter = mAdapter

        mRecyclerVIew.layoutManager = GridLayoutManager(this.context, 3)
        mContext = activity!!.applicationContext

        for (i in 1..10) {
            addItem("http://192.168.0.125:8080/upload/photos/b.jpg")
        }

        mAdapter.notifyDataSetChanged()

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
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    inner class ProfilePhotoProcessor: AsyncTask<String, Void, Bitmap>() {
        override fun doInBackground(vararg params: String?): Bitmap? {
            val url = URL(params[0])
            return try {
                BitmapFactory.decodeStream(url.openConnection().getInputStream())
            } catch (e: Exception) {
                println(e.stackTrace)
                null
            }

        }

        override fun onPostExecute(result: Bitmap?) {
            profileImage?.background = ShapeDrawable(OvalShape())
            profileImage?.clipToOutline = true
            profileImage?.let {
                it.setImageBitmap(result)
            }
        }
    }

    fun addItem(photoUri: String) {
        var item = MyPhotoItem()
        item.photoUri = photoUri
        mList.add(item)
    }

}
