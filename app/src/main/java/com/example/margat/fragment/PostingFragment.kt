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
import kotlinx.android.synthetic.main.fragment_posting.*

class PostingFragment : Fragment() {
    private var mListener: OnPostingFragmentInteractionListener? = null

    private lateinit var viewPager: ViewPager
    private var mContainer: ViewGroup? = null

    private var mAdapter: UploadImagePagerAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mContainer = container
        return inflater.inflate(R.layout.fragment_posting, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewPager = imagePager
        mAdapter = UploadImagePagerAdapter(fragmentManager!!)
        viewPager.adapter = mAdapter

        initializePostingFragment(mAdapter)
        imagePickButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            intent.type = MediaStore.Images.Media.CONTENT_TYPE
            activity?.startActivityForResult(intent, 1010)
        }

        postButton.setOnClickListener {
            when {
                postContent.text == null -> {
                    Toast.makeText(activity!!, "포스팅 내용을 입력해주세요!", Toast.LENGTH_SHORT).show()
                }
                mAdapter!!.getImageUrlList().size == 0 -> {
                    Toast.makeText(activity!!, "적어도 한 개 이상의 사진을 등록해주세요!", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    var main = activity as MainActivity
                    main.writePost()
                }
            }
        }

        clearButton.setOnClickListener {
            var main = activity as MainActivity
            main.clearScreen()
            main.deleteAllFiles()
            mAdapter!!.clearImages()
        }

        postContent.onFocusChangeListener = View.OnFocusChangeListener {v, hasFocus ->
            if (!hasFocus) {
                postContent.clearFocus()
                var inputMethodManager =
                    activity!!.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(v.windowToken, 0)
            }
        }
        prevImageButton.setOnClickListener {
            viewPager.currentItem--
        }
        nextImageButton.setOnClickListener {
            viewPager.currentItem++
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnPostingFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException("$context must implement OnPostingFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    private fun initializePostingFragment(item: Any?) {
        mListener?.onFragmentInteraction(item)
    }

    interface OnPostingFragmentInteractionListener {
        fun onFragmentInteraction(item: Any?)
    }

}
