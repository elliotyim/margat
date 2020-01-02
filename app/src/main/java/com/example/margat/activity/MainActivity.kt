package com.example.margat.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.margat.R
import com.example.margat.adapter.MyMessageRecyclerViewAdapter
import com.example.margat.const.Photo.Companion.PICK_FROM_GALLERY
import com.example.margat.controller.ContentViewPagerController
import com.example.margat.controller.PostController
import com.example.margat.controller.TabController
import com.example.margat.fragment.FeedFragment
import com.example.margat.fragment.MessageViewPagerFragment
import com.example.margat.fragment.PostingFragment
import com.example.margat.model.FeedContent
import com.example.margat.util.UriParser
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.tabs.TabLayout
import com.yalantis.ucrop.UCrop
import io.socket.client.Socket
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File


class MainActivity : AppCompatActivity(),
    FeedFragment.OnListFragmentInteractionListener,
    PostingFragment.OnPostingFragmentInteractionListener,
    MessageViewPagerFragment.OnMyMessageFragmentInteractionListener {

    private var mPostController: PostController? = null
    private lateinit var mSocket: Socket

    override fun onListFragmentInteraction(item: FeedContent.FeedItem?) {
        Toast.makeText(this, "클릭한 아이템의 내용은: ${item!!.content}", Toast.LENGTH_SHORT).show()
    }
    override fun onMyMessageFragmentInteraction(item: MyMessageRecyclerViewAdapter) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        var socketController = SocketController(this)
//        socketController.connectToServerSocket()
//        mSocket = socketController.getSocket()

        var tabController = TabController(this)
        var mTabLayout = tabController.createTabLayout()
        tabController.setEventListenersOnTabsWith(pager_content)


//        val badge: BadgeDrawable = mTabLayout.getTabAt(2)!!.orCreateBadge
//        badge.isVisible = true
//        badge.number = 99

        var fragmentManager = supportFragmentManager
        var contentsViewPagerController = ContentViewPagerController(this)
        contentsViewPagerController.createContentViewPagerWith(fragmentManager, mTabLayout)

        mPostController = PostController(this)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == PICK_FROM_GALLERY) {
            var sourceUri = data?.data as Uri
            var filePath = UriParser.getRealPathFromURI(this, sourceUri) as String

            var destinationPath  = "$filePath.tmp"
            var destinationFile = File(destinationPath)

            while (destinationFile.exists())
                destinationFile = File("$destinationPath.tmp")

            mPostController!!.cropImage(Uri.fromFile(File(filePath)), Uri.fromFile(destinationFile))

        } else if (resultCode == Activity.RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            mPostController!!.setImage(UCrop.getOutput(data!!)!!)
        }

        if (resultCode == UCrop.RESULT_ERROR && requestCode == UCrop.REQUEST_CROP)
            throw UCrop.getError(data!!)!!

    }

    override fun onDestroy() {
        super.onDestroy()
//        mSocket.close()
        mPostController!!.deleteAllFiles()
    }

    fun getPostController(): PostController {
        return mPostController!!
    }



}
