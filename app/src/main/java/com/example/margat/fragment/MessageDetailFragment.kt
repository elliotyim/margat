package com.example.margat.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.margat.R
import com.example.margat.model.Author
import com.stfalcon.chatkit.commons.ImageLoader
import com.stfalcon.chatkit.commons.models.IMessage
import com.stfalcon.chatkit.messages.MessageHolders
import com.stfalcon.chatkit.messages.MessagesListAdapter
import kotlinx.android.synthetic.main.fragment_message_detail.*
import java.sql.Date

class MessageDetailFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_message_detail, container, false)
    }

    override fun onStart() {
        super.onStart()



//        var imageLoader = ImageLoader { imageView, url, payload ->
//
//        }

//        var customHolder = MessageHolders()
//        customHolder.setOutcomingImageConfig(R.layout.item_dialog)

        var adapter = MessagesListAdapter<Message>("", null)
        messagesList.setAdapter(adapter)

        var messages = ArrayList<Message>()
        var user = Author("id", "name", "a.jpg")
        var user2 = Author("id2", "name2", "b.jpg")

        var message = Message("Id", "text", user, Date(System.currentTimeMillis()))
        var message2 = Message("Id2", "text2", user2, Date(System.currentTimeMillis()))

        messages.add(message)
        messages.add(message2)
        messages.add(message)
        messages.add(message2)
        messages.add(message)


        adapter.addToEnd(messages, false)
        adapter.notifyDataSetChanged()
    }
}