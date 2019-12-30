package com.example.margat.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.example.margat.R

class ImagePagerAdapter: PagerAdapter {

    private lateinit var mContainer: ViewGroup
    private var mContext: Context
    private var imageUriList = ArrayList<Uri>()

    constructor(context: Context) {
        this.mContext = context
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        mContainer = container
        var view: View? = null

        if (mContext != null) {
            var inflater =
                mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.image_page_item, container, false)

            var imageItem: ImageView = view.findViewById(R.id.imageItem)
            imageItem.setImageURI(imageUriList[position])
        }
        container.addView(view)
        return view as Any
    }

    override fun getCount(): Int {
        return imageUriList.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as View
    }
    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeView(obj as View)
    }

    fun addImage(uri: Uri) {
        imageUriList.add(uri)
    }

    fun removeImageAt(position: Int) {
        imageUriList.removeAt(position)
    }

    fun clearImages() {
        imageUriList.clear()
    }

    fun getImageUrlList(): ArrayList<Uri> {
        return imageUriList
    }

}