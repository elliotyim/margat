package com.example.margat.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.margat.R
import com.example.margat.model.Author
import com.example.margat.model.Message
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

        var messages = ArrayList<Message>()
        var message = Message("Id","text", Author("incoming", "name", "a.jpg"),
            Date(System.currentTimeMillis()))
        var message2 = Message("Id2", "text2", Author("outgoing", "name2", "b.jpg"),
            Date(System.currentTimeMillis()))

        for (i in 0..10) {
            messages.add(message)
            messages.add(message2)
        }

        var adapter = MessagesListAdapter<Message>("outgoing", null)
        messagesList.setAdapter(adapter)

        adapter.addToEnd(messages, false)
        adapter.notifyDataSetChanged()

        input.setInputListener { input ->
            adapter.addToStart(Message("Id2", input.toString(), Author("outgoing", "name2", "b.jpg"),
                Date(System.currentTimeMillis())), true)
            true
        }
    }
}