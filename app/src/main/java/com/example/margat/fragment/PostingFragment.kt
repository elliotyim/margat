package com.example.margat.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.margat.R
import com.example.margat.activity.MainActivity
import com.example.margat.adapter.UploadImagePagerAdapter
import com.example.margat.const.Photo.Companion.PICK_FROM_GALLERY
import com.example.margat.controller.PostController
import kotlinx.android.synthetic.main.fragment_posting.*

class PostingFragment : Fragment() {
    interface OnPostingFragmentInteractionListener
    private var mListener: OnPostingFragmentInteractionListener? = null

    private lateinit var mViewPager: ViewPager
    private lateinit var mImageAdapter: UploadImagePagerAdapter
    private lateinit var mPostController: PostController

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnPostingFragmentInteractionListener) {
            mListener = context
            mPostController = (mListener as MainActivity).getPostController()

        } else {
            throw RuntimeException("$context must implement OnPostingFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_posting, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mViewPager = imagePager
        mImageAdapter = UploadImagePagerAdapter(fragmentManager!!)
        mViewPager.adapter = mImageAdapter

        mPostController.loadInitialDependencies(this, mImageAdapter!!)

        setOnClickListenerToImagePickButton()
        setOnClickListenerToPostButton()
        setOnClickListenerToOnClearButton()
        setOnFocusChangeListenerToEditText()
        setOnClickListenerToPrevNextImageButton()
    }

    private fun setOnClickListenerToImagePickButton() {
        imagePickButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            intent.type = MediaStore.Images.Media.CONTENT_TYPE
            activity!!.startActivityForResult(intent, PICK_FROM_GALLERY)
        }
    }

    private fun setOnClickListenerToPostButton() {
        postButton.setOnClickListener {
            when {
                mImageAdapter!!.getImageUrlList().size == 0 -> {
                    Toast.makeText(activity!!, "적어도 한 개 이상의 사진을 등록해주세요!", Toast.LENGTH_SHORT).show()
                }
                postContent.text.toString().replace(" ", "") == "" -> {
                    Toast.makeText(activity!!, "포스팅 내용을 입력해주세요!", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    mPostController.writePost()
                }
            }
        }
    }

    private fun setOnClickListenerToOnClearButton() {
        clearButton.setOnClickListener {
            clearInputs()
            mPostController.deleteAllFiles()
            mImageAdapter!!.clearImages()
        }
    }

    private fun setOnFocusChangeListenerToEditText() {
        postContent.onFocusChangeListener = View.OnFocusChangeListener {v, hasFocus ->
            if (!hasFocus) {
                postContent.clearFocus()
                var inputMethodManager =
                    activity!!.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(v.windowToken, 0)
            }
        }
    }

    private fun setOnClickListenerToPrevNextImageButton() {
        prevImageButton.setOnClickListener { mViewPager.currentItem-- }
        nextImageButton.setOnClickListener { mViewPager.currentItem++ }
    }

    fun clearInputs() {
        imagePager.setBackgroundResource(R.drawable.transparent_background)

        postContent.text = null
        imageButtonLayout.visibility = View.GONE
        blankPager.visibility = View.VISIBLE
        imagePager.visibility = View.GONE
    }

}
