package com.example.margat.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.margat.R
import com.example.margat.const.Photo.Companion.PICK_FROM_GALLERY
import com.example.margat.controller.*
import com.example.margat.fragment.FeedFragment
import com.example.margat.fragment.MessageFragment
import com.example.margat.fragment.PostingFragment
import com.example.margat.item.FeedContent
import com.example.margat.item.MessageContent
import com.example.margat.util.UriParser
import com.yalantis.ucrop.UCrop
import io.socket.client.Socket
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File


class MainActivity : AppCompatActivity(),
    MessageFragment.OnListFragmentInteractionListener,
    FeedFragment.OnListFragmentInteractionListener,
    PostingFragment.OnPostingFragmentInteractionListener{

    private var mPostController: PostController? = null
    private lateinit var mSocket: Socket

    override fun onListFragmentInteraction(item: FeedContent.FeedItem?) {}
    override fun onListFragmentInteraction(item: MessageContent.MessageItem?) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var socketController = SocketController(this)
        socketController.connectToServerSocket()
        mSocket = socketController.getSocket()

        var tabController = TabController(this)
        var mTabLayout = tabController.createTabLayout()
        tabController.setEventListenersOnTabsWith(pager_content)

        var contentsViewPagerController = ContentViewPagerController(this)
        contentsViewPagerController.createContentViewPagerWith(mTabLayout)

        mPostController = PostController(this)

        var messageController = MessageController(this)


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
        mSocket.close()
        mPostController!!.deleteAllFiles()
    }

    fun getPostController(): PostController {
        return mPostController!!
    }



}
