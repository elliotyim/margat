package com.example.margat.fragment

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.margat.R
import kotlinx.android.synthetic.main.image_page_item.*

class UploadPhotoFragment: Fragment() {

    var mNum: Int = 0
    var uri: Uri? = null

    companion object {
        fun newInstance(num: Int, uri: Uri): UploadPhotoFragment {
            var f = UploadPhotoFragment()

            var args = Bundle()
            args.putInt("num", num)
            f.arguments = args
            f.uri = uri

            return f
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mNum = if (arguments != null) arguments!!.getInt("num") else 1
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var v = inflater.inflate(R.layout.image_page_item, container, false)
        var imageView = v.findViewById(R.id.imageItem) as ImageView

        imageView.setImageURI(uri)
        return v
    }
}