package com.example.margat.activity

import android.content.Context
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
import com.example.margat.adapter.ContentsPagerAdapter
import com.example.margat.adapter.ImagePagerAdapter
import com.example.margat.controller.PostingController
import com.example.margat.fragment.FeedFragment
import com.example.margat.fragment.MessageFragment
import com.example.margat.fragment.PostingFragment
import com.example.margat.item.FeedContent
import com.example.margat.item.MessageContent
import com.example.margat.util.MyCallback
import com.example.margat.util.RetrofitAPI
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.TabLayoutOnPageChangeListener
import com.yalantis.ucrop.UCrop
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

    private lateinit var mContext: Context
    private lateinit var mTabLayout: TabLayout

    private lateinit var mViewPager: ViewPager
    private lateinit var mContentPagerAdapter: ContentsPagerAdapter

    private lateinit var mImagePagerAdapter: ImagePagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mContext = applicationContext

        mTabLayout = layout_tab as TabLayout
        mTabLayout.addTab(mTabLayout.newTab().setIcon(R.drawable.ic_home_solid))
        mTabLayout.addTab(mTabLayout.newTab().setIcon(R.drawable.ic_plus_square_regular))
        mTabLayout.addTab(mTabLayout.newTab().setIcon(R.drawable.ic_paper_plane_regular))
        mTabLayout.addTab(mTabLayout.newTab().setIcon(R.drawable.ic_user_regular))

        mViewPager = pager_content
        mContentPagerAdapter =
            ContentsPagerAdapter(supportFragmentManager, mTabLayout.tabCount)
        mViewPager.adapter = mContentPagerAdapter
        mViewPager.addOnPageChangeListener(TabLayoutOnPageChangeListener(mTabLayout))

        mTabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                mViewPager.currentItem = tab?.position!!
                when (tab.position) {
                    0 -> tab.icon = applicationContext.getDrawable(R.drawable.ic_home_solid)
                    1 -> tab.icon = applicationContext.getDrawable(R.drawable.ic_plus_square_solid)
                    2 -> tab.icon = applicationContext.getDrawable(R.drawable.ic_paper_plane_solid)
                    3 -> tab.icon = applicationContext.getDrawable(R.drawable.ic_user_solid)
                }

            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> tab.icon = applicationContext.getDrawable(R.drawable.ic_home_regular)
                    1 -> tab.icon = applicationContext.getDrawable(R.drawable.ic_plus_square_regular)
                    2 -> tab.icon = applicationContext.getDrawable(R.drawable.ic_paper_plane_regular)
                    3 -> tab.icon = applicationContext.getDrawable(R.drawable.ic_user_regular)
                }
            }
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            if (requestCode == 1010) {
                var sourceUri = data?.data as Uri
                var filePath = getRealPathFromURI(sourceUri) as String
                var destinationPath  = "$filePath.tmp"
                var destinationFile = File(destinationPath)

                println(sourceUri)
                println(filePath)
                println(destinationPath)

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
                println("UCrop에서 성공적으로 넘어옴!")
                println(resultUri)

                mImagePagerAdapter.instantiateItem(imagePager,0)
                blankPager.visibility = View.GONE
                imagePager.visibility = View.VISIBLE
            }
        }
        if (resultCode == UCrop.RESULT_ERROR && requestCode == UCrop.REQUEST_CROP){
            throw UCrop.getError(data!!)!!
        }

        println("결과코드는: $resultCode")
        println("리퀘스트코드는: $requestCode")
//        writePost(filePath)

    }

    override fun onListFragmentInteraction(item: FeedContent.FeedItem?) {}
    override fun onListFragmentInteraction(item: MessageContent.MessageItem?) {}

    fun writePost() {
        var imageList = ArrayList<MultipartBody.Part>()
        var imageUriList = mImagePagerAdapter.getImageUrlList()

        for (i in 0 until imageUriList.size) {
            var file = imageUriList[i].toFile()
            var requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
            var uploadFile = MultipartBody.Part.createFormData("files", file.path, requestFile)
            imageList.add(uploadFile)
        }

        var map = HashMap<String, RequestBody>()
        var memberNo = getSharedPreferences("loginUser", 0).getInt("no", 0)
        var postContentBody = RequestBody.create(MediaType.parse("text/plain"), postContent.text.toString())
        map["memberNo"] = RequestBody.create(MediaType.parse("text/plain"), memberNo.toString())
        map["postContent"] = postContentBody

        var service = RetrofitAPI().creater.create(PostingController::class.java)
        service.writePostWithPhotos(map, imageList).enqueue(object: MyCallback<ResponseBody>() {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                println("수신 테스트")
                println(response.body())

                Toast.makeText(applicationContext, "성공적으로 포스팅이 완료되었습니다!", Toast.LENGTH_SHORT).show()

                imagePager.setBackgroundResource(R.drawable.transparent_background)
                postContent.text = null
                imageButtonLayout.visibility = View.GONE

                var imageUriList = mImagePagerAdapter.getImageUrlList()
                for (i in 0 until imageUriList.size)
                    imageUriList[i].toFile().delete()

                mImagePagerAdapter.clearImages()
                mImagePagerAdapter.notifyDataSetChanged()

                blankPager.visibility = View.VISIBLE
                imagePager.visibility = View.GONE

            }

        })

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

    override fun onFragmentInteraction(item: Any?) {
        this.mImagePagerAdapter = item as ImagePagerAdapter
    }

    fun setImage(uri: Uri) {
        mImagePagerAdapter.addImage(uri)
        mImagePagerAdapter.notifyDataSetChanged()
    }


}
