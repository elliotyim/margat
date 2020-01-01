package com.example.margat.controller

import android.net.Uri
import android.view.View
import android.widget.Toast
import androidx.core.net.toFile
import com.example.margat.activity.MainActivity
import com.example.margat.adapter.UploadImagePagerAdapter
import com.example.margat.fragment.PostingFragment
import com.example.margat.request.PostingRequest
import com.example.margat.util.MyCallback
import com.example.margat.util.RetrofitAPI
import com.yalantis.ucrop.UCrop
import kotlinx.android.synthetic.main.fragment_posting.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response

class PostController {

    var mActivity: MainActivity
    private lateinit var mPostingFragment: PostingFragment
    private lateinit var mImagePagerAdapter: UploadImagePagerAdapter

    constructor(mainActivity: MainActivity) {
        this.mActivity = mainActivity
    }

    fun loadInitialDependencies (
        postingFragment: PostingFragment,
        imagePagerAdapter: UploadImagePagerAdapter) {
        this.mPostingFragment = postingFragment
        this.mImagePagerAdapter = imagePagerAdapter
    }

    fun writePostWith() {
        var imageList = ArrayList<MultipartBody.Part>()
        var imageUriList = mImagePagerAdapter.getImageUrlList()

        for (i in 0 until imageUriList.size) {
            var file = imageUriList[i].toFile()
            var requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
            var uploadFile = MultipartBody.Part.createFormData("files", file.path, requestFile)
            imageList.add(uploadFile)
        }

        var map = HashMap<String, RequestBody>()
        var memberNo = mActivity.getSharedPreferences("loginUser", 0).getInt("no", 0)
        var postContentBody =
            RequestBody.create(MediaType.parse("text/plain"), mActivity.postContent.text.toString())
        map["memberNo"] = RequestBody.create(MediaType.parse("text/plain"), memberNo.toString())
        map["postContent"] = postContentBody

        var request = RetrofitAPI().creater.create(PostingRequest::class.java)
        request.writePostWithPhotos(map, imageList).enqueue(object: MyCallback<ResponseBody>() {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.code() == 200) {
                    deleteAllFiles()

                    mPostingFragment.clearInputs()
                    mImagePagerAdapter.clearImages()
                    mImagePagerAdapter.notifyDataSetChanged()

                    Toast.makeText(mActivity, "성공적으로 포스팅이 완료되었습니다!", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    fun deleteAllFiles() {
        var imageUriList = mImagePagerAdapter.getImageUrlList()
        for (i in 0 until imageUriList.size)
            imageUriList[i].toFile().delete()
    }

    fun setImage(uri: Uri) {
        mImagePagerAdapter.addItem(uri)
        mImagePagerAdapter.notifyDataSetChanged()

        mImagePagerAdapter.instantiateItem(mActivity.imagePager,0)
        mActivity.blankPager.visibility = View.GONE
        mActivity.imagePager.visibility = View.VISIBLE
    }

    fun cropImage(newUri: Uri, destinationUri: Uri) {
        if (mActivity.imagePager.background != null)
            mActivity.imagePager.setBackgroundResource(0)
        mActivity.imageButtonLayout.visibility = View.VISIBLE

        UCrop.of(newUri, destinationUri)
            .withAspectRatio(1F, 1F)
            .withMaxResultSize(500, 500)
            .start(mActivity)
    }

}