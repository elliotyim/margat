package com.example.margat.adapter

import android.net.Uri
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import com.example.margat.fragment.UploadPhotoFragment

class UploadImagePagerAdapter: FragmentStatePagerAdapter {

    private var pageIndexes: ArrayList<Int> = ArrayList()
    private var imageUriList: ArrayList<Uri> = ArrayList()

    constructor(fm: FragmentManager): super(fm) {
        clearImages()
    }

    override fun getCount(): Int {
        return pageIndexes.size
    }
    override fun getItem(position: Int): Fragment {
        var index = pageIndexes[position]
        return UploadPhotoFragment.newInstance(index, imageUriList[position]) // 수정
    }

    fun deletePage(position: Int) {
        if (canDelete()) {
            pageIndexes.removeAt(position)
            notifyDataSetChanged()
        } else {
            clearImages()
        }
    }

    fun canDelete(): Boolean {
        return pageIndexes.size > 0
    }

    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }

    fun addItem(imageUri: Uri) {
        pageIndexes.add(pageIndexes.size)
        imageUriList.add(imageUri)
        notifyDataSetChanged()
    }

    fun getImageUrlList(): ArrayList<Uri> {
        return imageUriList
    }

    fun clearImages() {
        pageIndexes.clear()
        imageUriList.clear()
        notifyDataSetChanged()
    }

}