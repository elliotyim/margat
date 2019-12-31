package com.example.margat.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toFile
import androidx.loader.content.CursorLoader
import androidx.viewpager.widget.ViewPager
import com.example.margat.R
import com.example.margat.adapter.UploadImagePagerAdapter
import com.example.margat.adapter.ContentsPagerAdapter
import com.example.margat.const.Photo.Companion.PICK_FROM_GALLERY
import com.example.margat.controller.ContentViewPagerController
import com.example.margat.controller.SocketController
import com.example.margat.request.PostingRequest
import com.example.margat.fragment.FeedFragment
import com.example.margat.fragment.MessageFragment
import com.example.margat.fragment.PostingFragment
import com.example.margat.item.FeedContent
import com.example.margat.item.MessageContent
import com.example.margat.util.MyCallback
import com.example.margat.util.RetrofitAPI
import com.example.margat.controller.TabController
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.TabLayoutOnPageChangeListener
import com.yalantis.ucrop.UCrop
import io.socket.client.Socket
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_posting.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import java.io.File


class MainActivity : AppCompatActivity(),
    MessageFragment.OnListFragmentInteractionListener,
    FeedFragment.OnListFragmentInteractionListener,
    PostingFragment.OnPostingFragmentInteractionListener{

    private lateinit var mTabLayout: TabLayout

    private lateinit var mContentsViewPager: ViewPager
    private lateinit var mContentPagerAdapter: ContentsPagerAdapter

    private lateinit var mImagePagerAdapter: UploadImagePagerAdapter

    private lateinit var mSocket: Socket

    override fun onListFragmentInteraction(item: FeedContent.FeedItem?) {}
    override fun onListFragmentInteraction(item: MessageContent.MessageItem?) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        var socketController = SocketController(this)
//        socketController.connectToServerSocket()


        var tabController = TabController(this)
        mTabLayout = tabController.createTabLayout()
        tabController.setEventListenersOnTabsWith(pager_content)

        var contentsViewPagerController = ContentViewPagerController(this)
        mContentsViewPager = contentsViewPagerController.createContentViewPager(mTabLayout)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_FROM_GALLERY) {
                var sourceUri = data?.data as Uri
                var filePath = getRealPathFromURI(sourceUri) as String
                var destinationPath  = "$filePath.tmp"
                var destinationFile = File(destinationPath)

                while (destinationFile.exists()) {
                    destinationPath+=".tmp"
                    destinationFile = File(destinationPath)
                }

                var newUri = Uri.fromFile(File(filePath))
                var destinationUri = Uri.fromFile(destinationFile)

                if (imagePager.background != null)
                    imagePager.setBackgroundResource(0)
                imageButtonLayout.visibility = View.VISIBLE

                UCrop.of(newUri, destinationUri)
                    .withAspectRatio(1F, 1F)
                    .withMaxResultSize(500, 500)
                    .start(this)

            } else if (requestCode == UCrop.REQUEST_CROP) {
                var resultUri = UCrop.getOutput(data!!)!!
                setImage(resultUri)

                mImagePagerAdapter.instantiateItem(imagePager,0)
                blankPager.visibility = View.GONE
                imagePager.visibility = View.VISIBLE
            }
        }
        if (resultCode == UCrop.RESULT_ERROR && requestCode == UCrop.REQUEST_CROP){
            throw UCrop.getError(data!!)!!
        }

    }

    override fun onFragmentInteraction(item: Any?) {
        this.mImagePagerAdapter = item as UploadImagePagerAdapter
    }

    private fun getRealPathFromURI(fileUri: Uri): String? {
        var proj: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
        var loader = CursorLoader(application, fileUri, proj, null, null, null)
        var cursor = loader.loadInBackground()
        var columnIndex = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor?.moveToFirst()
        var result = cursor?.getString(columnIndex!!)
        cursor?.close()
        return result
    }

    fun setImage(uri: Uri) {
        mImagePagerAdapter.addItem(uri)
        mImagePagerAdapter.notifyDataSetChanged()
    }

    fun clearInputs() {
        imagePager.setBackgroundResource(R.drawable.transparent_background)
        postContent.text = null
        imageButtonLayout.visibility = View.GONE

        blankPager.visibility = View.VISIBLE
        imagePager.visibility = View.GONE
    }



}
